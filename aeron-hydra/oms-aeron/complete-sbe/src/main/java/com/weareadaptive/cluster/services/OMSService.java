package com.weareadaptive.cluster.services;

import static com.weareadaptive.util.BufferUtils.E_CLEAR_Encoder;
import static com.weareadaptive.util.BufferUtils.E_RESET_Encoder;

import java.io.IOException;

import com.weareadaptive.cluster.services.oms.Order;
import com.weareadaptive.cluster.services.oms.OrderbookImpl;
import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.util.BufferUtils;
import com.weareadaptive.util.EncodeResult;
import com.weareadaptive.util.SnapshotManager;

import org.agrona.DirectBuffer;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.cluster.service.ClientSession;

public class OMSService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OMSService.class);
    private final IdleStrategy idleStrategy = new BusySpinIdleStrategy();
    private final OrderbookImpl orderbook;
    private final SnapshotManager snapshotManager;

    public OMSService()
    {
        orderbook = new OrderbookImpl();
        snapshotManager = new SnapshotManager(orderbook);
    }

    /**
     * * Receive Ingress binary encoding and place order in Orderbook
     * - Decode buffer
     * - Perform business logic
     * - Encode a response
     * - Offer Egress back to cluster client
     */
    public void placeOrder(final ClientSession session, final long correlation, final DirectBuffer buffer, final int offset)
    {
        final Order allocatedOrder = BufferUtils.I_PO_Decoder(buffer, offset);
        final ExecutionResult result = orderbook.placeOrder(allocatedOrder.getPrice(), allocatedOrder.getSize(), allocatedOrder.getSide());
        LOGGER.info("Ingress-" + correlation + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());
        final EncodeResult encodeResult = BufferUtils.E_PO_Encoder(correlation, result.getOrderId(), result.getStatus());
        while (session.offer(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength()) < 0)
        {
            idleStrategy.idle();
        }
    }

    /**
     * * Receive Ingress binary encoding and cancel order in Orderbook
     * - Decode buffer
     * - Perform business logic
     * - Encode a response
     * - Offer Egress back to cluster client
     */
    public void cancelOrder(final ClientSession session, long correlation, DirectBuffer buffer, int offset)
    {
        final long orderId = BufferUtils.I_CO_Decoder(buffer, offset);
        final ExecutionResult result = orderbook.cancelOrder(orderId);
        LOGGER.info("Ingress-" + correlation + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());
        final EncodeResult encodeResult = BufferUtils.E_CO_Encoder(correlation, result.getOrderId(), result.getStatus());
        while (session.offer(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength()) < 0)
        {
            idleStrategy.idle();
        }
    }

    /**
     * * Receive Ingress binary encoding and clear Orderbook
     * - Decode buffer
     * - Perform business logic
     * - Encode a response
     * - Offer Egress back to cluster client
     */
    public void clearOrderbook(final ClientSession session, final long correlation, final DirectBuffer buffer, final int offset)
    {
        orderbook.clear();
        LOGGER.info("Ingress-" + correlation + " | Cleared Orderbook");
        final EncodeResult encodeResult = E_CLEAR_Encoder(correlation);
        while (session.offer(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength()) < 0)
        {
            idleStrategy.idle();
        }
    }

    /**
     * * Receive Ingress binary encoding and reset Orderbook
     * - Decode buffer
     * - Perform business logic
     * - Encode a response
     * - Offer Egress back to cluster client
     */
    public void resetOrderbook(ClientSession session, long correlation, DirectBuffer buffer, int offset)
    {
        orderbook.reset();
        LOGGER.info("Ingress-" + correlation + " | Reset Orderbook");
        final EncodeResult encodeResult = E_RESET_Encoder(correlation);
        while (session.offer(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength()) < 0)
        {
            idleStrategy.idle();
        }
    }

    /**
     * * Encode current orderbook state and offer to SnapshotPublication
     * - Convert data structures in Orderbook for encoding
     * - Encode Orderbook state
     * - Offer to SnapshotPublication
     */
    public void onTakeSnapshot(ExclusivePublication snapshotPublication) throws IOException
    {
        snapshotManager.takeSnapshot(snapshotPublication);
    }

    /**
     * * Decode Snapshot Image and restore Orderbook state
     * - Decode Snapshot Image encoding into appropriate data structures
     * - Restore into Orderbook state
     */
    public void onRestoreSnapshot(final Image snapshotImage)
    {
        snapshotManager.loadSnapshot(snapshotImage);
    }
}
