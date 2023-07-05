package com.weareadaptive.gateway.clientLogic;

import static com.weareadaptive.util.SetupConfigUtils.localHost;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.weareadaptive.oms.util.ExecutionResult;
import com.weareadaptive.sbe.BufferUtils;
import com.weareadaptive.sbe.CancelOrderEgressDecoder;
import com.weareadaptive.sbe.ClearOrderbookEgressDecoder;
import com.weareadaptive.sbe.MessageHeaderDecoder;
import com.weareadaptive.sbe.OrderEgressDecoder;
import com.weareadaptive.sbe.ResetOrderbookEgressDecoder;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.Subscription;
import io.aeron.cluster.client.AeronCluster;
import io.aeron.cluster.client.EgressListener;
import io.aeron.cluster.codecs.EventCode;
import io.aeron.logbuffer.Header;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

/**
 * Listener logic for Egress messages from the cluster
 */
public class ClientEgressListener implements EgressListener
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientEgressListener.class);
    private final HashMap<Long, ServerWebSocket> wsSessions = new HashMap<>();
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final PendingMessageManager pendingMessageManager;
    private AeronCluster cluster;
    private int currentLeader = -1;
    private String leaderEndpoint;
    private Subscription mdcSub;

    public ClientEgressListener(final PendingMessageManager pendingMessageManager)
    {
        this.pendingMessageManager = pendingMessageManager;
    }

    /**
     * * Implement Egress routing logic
     * - Decode buffer start for command and correlationId
     * - Route to logic based on command
     */
    @Override
    public void onMessage(
        final long clusterSessionId,
        final long timestamp,
        final DirectBuffer buffer,
        final int offset,
        final int length,
        final Header header
    )
    {
        headerDecoder.wrap(buffer, offset);
        final long correlationId = headerDecoder.correlationId();
        pendingMessageManager.markMessageAsReceived(correlationId);
        switch (headerDecoder.templateId())
        {
            case OrderEgressDecoder.TEMPLATE_ID -> placeOrderListener(correlationId, buffer, offset);
            case CancelOrderEgressDecoder.TEMPLATE_ID -> cancelOrderListener(correlationId, buffer, offset);
            case ClearOrderbookEgressDecoder.TEMPLATE_ID -> clearOrderbookListener(correlationId, buffer, offset);
            case ResetOrderbookEgressDecoder.TEMPLATE_ID -> resetOrderbookListener(correlationId, buffer, offset);
        }
    }

    public void registerMDCSubscription(final String mdcHost, final AeronCluster cluster)
    {
        //Multi-Destination-Cast
        if (this.cluster == null)
        {
            this.cluster = cluster;
        }
        final String host = localHost("localhost");
        LOGGER.info(mdcHost);
        final int controlChannelPort = 40457;
        final String channel = "aeron:udp?endpoint=" + host + ":0|control=" + mdcHost + ":" + controlChannelPort + "|control-mode=dynamic";
        mdcSub = cluster.context().aeron().addSubscription(channel, 100);
    }

    @Override
    public void onSessionEvent(
        final long correlationId,
        final long clusterSessionId,
        final long leadershipTermId,
        final int leaderMemberId,
        final EventCode code,
        final String detail
    )
    {
    }

    @Override
    public void onNewLeader(
        final long clusterSessionId,
        final long leadershipTermId,
        final int leaderMemberId,
        final String ingressEndpoints
    )
    {
        LOGGER.info("Cluster node " + leaderMemberId + " is now Leader, previous Leader: " + currentLeader);
        leaderEndpoint = getAddressByIndex(ingressEndpoints, leaderMemberId);
        LOGGER.info("New Leader Endpoint: " + leaderEndpoint);
        currentLeader = leaderMemberId;

        if (mdcSub != null)
        {
            mdcSub.close();
            mdcSub = null;
            registerMDCSubscription(leaderEndpoint, cluster);
        }
        else
        {
            registerMDCSubscription(leaderEndpoint, cluster);
        }
    }

    public String getAddressByIndex(String addressString, int index)
    {
        String[] addresses = addressString.split(",");
        for (String address : addresses)
        {
            String[] parts = address.split("=");
            if (Integer.parseInt(parts[0]) == index)
            {
                String[] addressParts = parts[1].split(":");
                return addressParts[0]; // Return address without port
            }
        }
        return null; // Index not found
    }

    /**
     * * Implement Place Order Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlationId WS
     *
     * @param correlationId - Message correlationId ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void placeOrderListener(
        final long correlationId,
        final DirectBuffer buffer,
        final int offset
    )
    {
        final ExecutionResult result = BufferUtils.E_PO_Decoder(buffer, offset);
        LOGGER.info("Egress-" + correlationId + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());

        final JsonObject orderResponse = new JsonObject();
        orderResponse.put("OrderId", result.getOrderId());
        orderResponse.put("Status", result.getStatus());
        wsSessions.get(correlationId).writeFinalTextFrame(orderResponse.encode());
    }

    /**
     * * Implement Cancel Order Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlationId WS
     *
     * @param correlationId - Message correlationId ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void cancelOrderListener(
        final long correlationId,
        final DirectBuffer buffer,
        final int offset
    )
    {
        final ExecutionResult result = BufferUtils.E_CO_Decoder(buffer, offset);
        LOGGER.info("Egress-" + correlationId + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());

        final JsonObject orderResponse = new JsonObject();
        orderResponse.put("OrderId", result.getOrderId());
        orderResponse.put("Status", result.getStatus());
        wsSessions.get(correlationId).writeFinalTextFrame(orderResponse.encode());
    }

    /**
     * * Implement Clear Orderbook Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlationId WS
     *
     * @param correlationId - Message correlationId ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void clearOrderbookListener(
        final long correlationId,
        final DirectBuffer buffer,
        final int offset
    )
    {
        LOGGER.info("correlationId ID: " + correlationId);

        final JsonObject response = new JsonObject();
        response.put("Status", "SUCCESS");
        wsSessions.get(correlationId).writeFinalTextFrame(response.encode());
    }

    /**
     * * Implement Reset Orderbook Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlationId WS
     *
     * @param correlationId - Message correlationId ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void resetOrderbookListener(
        final long correlationId,
        final DirectBuffer buffer,
        final int offset
    )
    {
        LOGGER.info("correlationId ID: " + correlationId);

        final JsonObject response = new JsonObject();
        response.put("Status", "SUCCESS");
        wsSessions.get(correlationId).writeFinalTextFrame(response.encode());
    }

    /**
     * Add WS session to correlationId key hashmap for returning response after receiving egress
     *
     * @param correlationId - Message correlationId ID
     * @param ws          - Web socket session
     */
    public void addWSSession(
        final long correlationId,
        final ServerWebSocket ws
    )
    {
        wsSessions.put(correlationId, ws);
    }

    /**
     * Remove WS session from hashmap if no longer connected
     *
     * @param ws - Web socket session
     */
    public void removeWSSessions(final ServerWebSocket ws)
    {
        final Iterator<Map.Entry<Long, ServerWebSocket>> iterator = wsSessions.entrySet().iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<Long, ServerWebSocket> entry = iterator.next();
            if (ws.equals(entry.getValue()))
            {
                iterator.remove();
            }
        }
    }

    /**
     * @return current cluster leader ID
     */
    public int getCurrentLeader()
    {
        return this.currentLeader;
    }

    public void doWork()
    {
        if (mdcSub.isConnected())
        {
            mdcSub.poll((buffer, offset, length, header) ->
            {
                final var read = buffer.getLong(offset);
                LOGGER.info("received from MDC {}", read);
            }, 100);
        }
    }

}
