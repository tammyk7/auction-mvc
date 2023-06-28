package com.weareadaptive.gateway.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.sbe.CancelOrderEgressDecoder;
import com.weareadaptive.sbe.ClearOrderbookEgressDecoder;
import com.weareadaptive.sbe.MessageHeaderDecoder;
import com.weareadaptive.sbe.OrderEgressDecoder;
import com.weareadaptive.sbe.ResetOrderbookEgressDecoder;
import com.weareadaptive.util.BufferOffsets;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private int currentLeader = -1;

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
        final long correlation = headerDecoder.correlation();
        switch (headerDecoder.templateId())
        {
            case OrderEgressDecoder.TEMPLATE_ID -> placeOrderListener(correlation, buffer, offset);
            case CancelOrderEgressDecoder.TEMPLATE_ID -> cancelOrderListener(correlation, buffer, offset);
            case ClearOrderbookEgressDecoder.TEMPLATE_ID -> clearOrderbookListener(correlation, buffer, offset);
            case ResetOrderbookEgressDecoder.TEMPLATE_ID -> resetOrderbookListener(correlation, buffer, offset);
        }
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
        currentLeader = leaderMemberId;
    }

    /**
     * * Implement Place Order Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlation WS
     *
     * @param correlation - Message correlation ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void placeOrderListener(
        final long correlation,
        final DirectBuffer buffer,
        final int offset
    )
    {
        final ExecutionResult result = BufferOffsets.E_PO_Decoder(buffer, offset);
        LOGGER.info("Egress-" + correlation + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());

        final JsonObject orderResponse = new JsonObject();
        orderResponse.put("OrderId", result.getOrderId());
        orderResponse.put("Status", result.getStatus());
        wsSessions.get(correlation).writeFinalTextFrame(orderResponse.encode());
    }

    /**
     * * Implement Cancel Order Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlation WS
     *
     * @param correlation - Message correlation ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void cancelOrderListener(
        final long correlation,
        final DirectBuffer buffer,
        final int offset
    )
    {
        final ExecutionResult result = BufferOffsets.E_CO_Decoder(buffer, offset);
        LOGGER.info("Egress-" + correlation + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());

        final JsonObject orderResponse = new JsonObject();
        orderResponse.put("OrderId", result.getOrderId());
        orderResponse.put("Status", result.getStatus());
        wsSessions.get(correlation).writeFinalTextFrame(orderResponse.encode());
    }

    /**
     * * Implement Clear Orderbook Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlation WS
     *
     * @param correlation - Message correlation ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void clearOrderbookListener(
        final long correlation,
        final DirectBuffer buffer,
        final int offset
    )
    {
        LOGGER.info("Correlation ID: " + correlation);

        final JsonObject response = new JsonObject();
        response.put("Status", "SUCCESS");
        wsSessions.get(correlation).writeFinalTextFrame(response.encode());
    }

    /**
     * * Implement Reset Orderbook Response Egress Listener logic
     * - Decode buffer
     * - Encode response into JSON
     * - Send response to correlation WS
     *
     * @param correlation - Message correlation ID
     * @param buffer      - Message buffer
     * @param offset      - Message buffer offset
     */
    private void resetOrderbookListener(
        final long correlation,
        final DirectBuffer buffer,
        final int offset
    )
    {
        LOGGER.info("Correlation ID: " + correlation);

        final JsonObject response = new JsonObject();
        response.put("Status", "SUCCESS");
        wsSessions.get(correlation).writeFinalTextFrame(response.encode());
    }

    /**
     * Add WS session to correlation key hashmap for returning response after receiving egress
     *
     * @param correlation - Message correlation ID
     * @param ws          - Web socket session
     */
    public void addWSSession(
        final long correlation,
        final ServerWebSocket ws
    )
    {
        wsSessions.put(correlation, ws);
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

}
