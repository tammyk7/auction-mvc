package com.weareadaptive.gateway.client;

import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.util.BufferOffsets;
import io.aeron.cluster.client.EgressListener;
import io.aeron.cluster.codecs.EventCode;
import io.aeron.logbuffer.Header;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ClientEgressListener implements EgressListener
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientEgressListener.class);
    HashMap<Long, ServerWebSocket> WSSessions = new HashMap<>();
    private int currentLeader = -1;

    /**
     * * Implement Egress routing logic
     *      - Decode buffer start for command and correlation
     *      - Route to logic based on command
     */
    @Override
    public void onMessage(final long clusterSessionId, final long timestamp, final DirectBuffer buffer, final int offset, final int length, final Header header)
    {
        byte command = buffer.getByte(offset + BufferOffsets.COMMAND_OFFSET);
        long correlation = buffer.getLong(offset + BufferOffsets.CORRELATION_OFFSET);
        switch(command)
        {
            case 1 -> PlaceOrderListener(correlation, buffer, offset);
            case 2 -> CancelOrderListener(correlation, buffer, offset);
            case 3 -> ClearOrderbookListener(correlation, buffer, offset);
            case 4 -> ResetOrderbookListener(correlation, buffer, offset);
        }
    }

    @Override
    public void onSessionEvent(long correlationId, long clusterSessionId, long leadershipTermId, int leaderMemberId, EventCode code, String detail)
    {
    }

    @Override
    public void onNewLeader(long clusterSessionId, long leadershipTermId, int leaderMemberId, String ingressEndpoints) {
        LOGGER.info("Cluster node " + leaderMemberId + " is now Leader, previous Leader: " + currentLeader);
        currentLeader = leaderMemberId;
    }

    /**
     * * Implement Place Order Response Egress Listener logic
     *      - Decode buffer
     *      - Encode response into JSON
     *      - Send response to correlation WS
     */
    private void PlaceOrderListener(final long correlation, final DirectBuffer buffer, final int offset)
    {
        ExecutionResult result = BufferOffsets.E_PO_Decoder(buffer, offset);
        LOGGER.info("Egress-" + correlation + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());

        JsonObject orderResponse = new JsonObject();
        orderResponse.put("OrderId", result.getOrderId());
        orderResponse.put("Status", result.getStatus());
        WSSessions.get(correlation).writeFinalTextFrame(orderResponse.encode());
    }

    /**
     * * Implement Cancel Order Response Egress Listener logic
     *      - Decode buffer
     *      - Encode response into JSON
     *      - Send response to correlation WS
     */
    private void CancelOrderListener(final long correlation, final DirectBuffer buffer, final int offset)
    {
        ExecutionResult result = BufferOffsets.E_CO_Decoder(buffer, offset);
        LOGGER.info("Egress-" + correlation + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());

        JsonObject orderResponse = new JsonObject();
        orderResponse.put("OrderId", result.getOrderId());
        orderResponse.put("Status", result.getStatus());
        WSSessions.get(correlation).writeFinalTextFrame(orderResponse.encode());
    }

    /**
     * * Implement Clear Orderbook Response Egress Listener logic
     *      - Decode buffer
     *      - Encode response into JSON
     *      - Send response to correlation WS
     */
    private void ClearOrderbookListener(final long correlation, final DirectBuffer buffer, final int offset)
    {
        LOGGER.info("Correlation ID: " + correlation);

        JsonObject response = new JsonObject();
        response.put("Status", "SUCCESS");
        WSSessions.get(correlation).writeFinalTextFrame(response.encode());
    }

    /**
     * * Implement Reset Orderbook Response Egress Listener logic
     *      - Decode buffer
     *      - Encode response into JSON
     *      - Send response to correlation WS
     */
    private void ResetOrderbookListener(final long correlation, final DirectBuffer buffer, final int offset)
    {
        LOGGER.info("Correlation ID: " + correlation);

        JsonObject response = new JsonObject();
        response.put("Status", "SUCCESS");
        WSSessions.get(correlation).writeFinalTextFrame(response.encode());
    }

    /**
     * Add WS session to correlation key hashmap for returning response after receiving egress
     */
    public void addWSSession(final long correlation, final ServerWebSocket WS)
    {
        WSSessions.put(correlation, WS);
    }

    /**
     * Remove WS session from hashmap if no longer connected
     */
    public void removeWSSessions(final ServerWebSocket WS)
    {
        Iterator<Map.Entry<Long, ServerWebSocket>> iterator = WSSessions.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Long, ServerWebSocket> entry = iterator.next();
            if (WS.equals(entry.getValue()))
            {
                iterator.remove();
            }
        }
    }

    public int getCurrentLeader()
    {
        return this.currentLeader;
    }

}
