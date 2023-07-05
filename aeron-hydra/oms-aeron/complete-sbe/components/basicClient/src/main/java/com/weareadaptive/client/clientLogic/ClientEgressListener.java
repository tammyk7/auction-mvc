package com.weareadaptive.client.clientLogic;

import static com.weareadaptive.util.SetupConfigUtils.localHost;

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

/**
 * Listener logic for Egress messages from the cluster
 */
public class ClientEgressListener implements EgressListener
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientEgressListener.class);
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final PendingMessageManager pendingMessageManager;
    private AeronCluster cluster;
    //    private final AeronCluster cluster;
    private int currentLeader = -1;
    private String leaderEndpoint;
    private Subscription mdcSub;


    public ClientEgressListener(final PendingMessageManager pendingMessageManager)
    {
        this.pendingMessageManager = pendingMessageManager;
    }

    /**
     * * Implement Egress routing logic
     * - Decode buffer start for command and correlation
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
     * - Send response to correlation WS
     *
     * @param correlationId - Message correlation ID
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
    }

    /**
     * * Implement Cancel Order Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlationId WS
     *
     * @param correlationId - Message correlation ID
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
    }

    /**
     * * Implement Clear Orderbook Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlation WS
     *
     * @param correlationId - Message correlation ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void clearOrderbookListener(
        final long correlationId,
        final DirectBuffer buffer,
        final int offset
    )
    {
        LOGGER.info("Correlation ID: " + correlationId);
    }

    /**
     * * Implement Reset Orderbook Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlation WS
     *
     * @param correlationId - Message correlation ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void resetOrderbookListener(
        final long correlationId,
        final DirectBuffer buffer,
        final int offset
    )
    {
        LOGGER.info("Correlation ID: " + correlationId);
    }

    /**
     * @return current cluster leader ID
     */
    public int getCurrentLeader()
    {
        return this.currentLeader;
    }

    /**
     * @return current leader node endpoint
     */
    public String getLeaderEndpoint()
    {
        return this.leaderEndpoint;
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
