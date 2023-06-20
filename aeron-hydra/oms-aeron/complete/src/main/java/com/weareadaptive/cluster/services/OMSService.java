package com.weareadaptive.cluster.services;

import com.weareadaptive.cluster.services.oms.Order;
import com.weareadaptive.cluster.services.oms.OrderbookImpl;
import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.util.BufferOffsets;
import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.cluster.service.ClientSession;
import org.agrona.DirectBuffer;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.weareadaptive.util.BufferOffsets.*;

public class OMSService
{

    private IdleStrategy idleStrategy = new BusySpinIdleStrategy();
    private OrderbookImpl orderbook = new OrderbookImpl();
    public OMSService() {}
    private static final Logger LOGGER = LoggerFactory.getLogger(OMSService.class);

    /**
     * * Receive Ingress binary encoding and place order in Orderbook
     *      - Decode buffer
     *      - Perform business logic
     *      - Encode a response
     *      - Offer Egress back to cluster client
     *
     *      - Backpressure
     */
    public void placeOrder(ClientSession session, long correlation, DirectBuffer buffer, int offset)
    {
        Order allocatedOrder = BufferOffsets.I_PO_Decoder(buffer, offset);
        ExecutionResult result = orderbook.placeOrder(allocatedOrder.getPrice(),allocatedOrder.getSize(),allocatedOrder.getSide());
        LOGGER.info("Ingress-" + correlation + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());
        UnsafeBuffer returnBuffer = BufferOffsets.E_PO_Encoder(correlation, result.getOrderId(), result.getStatus());
        while (session.offer(returnBuffer, 0, E_PO_LENGTH) < 0);
    }

    /**
     * * Receive Ingress binary encoding and cancel order in Orderbook
     *      - Decode buffer
     *      - Perform business logic
     *      - Encode a response
     *      - Offer Egress back to cluster client
     */
    public void cancelOrder(ClientSession session, long correlation, DirectBuffer buffer, int offset)
    {
        long orderId = BufferOffsets.I_CO_Decoder(buffer, offset);
        ExecutionResult result = orderbook.cancelOrder(orderId);
        LOGGER.info("Ingress-" + correlation + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());
        UnsafeBuffer returnBuffer = BufferOffsets.E_CO_Encoder(correlation, result.getOrderId(), result.getStatus());
        while (session.offer(returnBuffer, 0, E_CO_LENGTH) < 0);
    }

    /**
     * * Receive Ingress binary encoding and clear Orderbook
     *      - Decode buffer
     *      - Perform business logic
     *      - Encode a response
     *      - Offer Egress back to cluster client
     */
    public void clearOrderbook(ClientSession session, long correlation, DirectBuffer buffer, int offset)
    {
        orderbook.clear();
        LOGGER.info("Ingress-" + correlation + " | Cleared Orderbook");
        UnsafeBuffer returnBuffer = E_CLEAR_Encoder(correlation);
        while (session.offer(returnBuffer, 0, HEADER_LENGTH) < 0);
    }

    /**
     * * Receive Ingress binary encoding and reset Orderbook
     *      - Decode buffer
     *      - Perform business logic
     *      - Encode a response
     *      - Offer Egress back to cluster client
     */
    public void resetOrderbook(ClientSession session, long correlation, DirectBuffer buffer, int offset)
    {
        orderbook.reset();
        LOGGER.info("Ingress-" + correlation + " | Reset Orderbook");
        UnsafeBuffer returnBuffer = E_RESET_Encoder(correlation);
        while (session.offer(returnBuffer, 0, HEADER_LENGTH) < 0);
    }

    /**
     * * Encode current orderbook state and offer to SnapshotPublication
     *      - Convert data structures in Orderbook for encoding
     *      - Encode Orderbook state
     *      - Offer to SnapshotPublication
     */
    public void onTakeSnapshot(ExclusivePublication snapshotPublication) throws IOException
    {
        UnsafeBuffer snapshotBuffer = BufferOffsets.SNAPSHOT_ENCODER(orderbook, getOrderbookStateBytes());
        while (snapshotPublication.offer(snapshotBuffer, 0, snapshotBuffer.capacity()) < 0);
        LOGGER.info("Cluster Snapshot taken");
    }

    /**
     * * Decode Snapshot Image and restore Orderbook state
     *      - Decode Snapshot Image encoding into appropriate data structures
     *      - Restore into Orderbook state
     */
    public void onRestoreSnapshot(Image snapshotImage) throws IOException, ClassNotFoundException
    {
        final MutableDirectBuffer totalSnapshotBuffer = new ExpandableArrayBuffer();
        AtomicInteger snapshotOffset = new AtomicInteger();

        while (!snapshotImage.isEndOfStream())
        {
            final int polledFragments = snapshotImage.poll((bufferFragment, offset, length, header) ->
                {
                    byte[] bytes = new byte[length];
                    bufferFragment.getBytes(offset,bytes);
                    totalSnapshotBuffer.putBytes(snapshotOffset.get(), bytes);
                    snapshotOffset.set(snapshotOffset.get() + length);
                },
            Integer.MAX_VALUE);

            idleStrategy.idle(polledFragments);
        }

        assert snapshotImage.isEndOfStream();

        SNAPSHOT_DECODER_LOAD_STATE(orderbook, totalSnapshotBuffer);
        LOGGER.info("Cluster Snapshot Restored");
    }

    public HashMap<String,byte[]> getOrderbookStateBytes() throws IOException
    {
        HashMap<String, byte[]> states = new HashMap<>();
        states.put("orders", convertToByteArray(orderbook.getOrders()));
        states.put("bids", convertToByteArray(orderbook.getBids()));
        states.put("asks", convertToByteArray(orderbook.getAsks()));
        return states;
    }
}
