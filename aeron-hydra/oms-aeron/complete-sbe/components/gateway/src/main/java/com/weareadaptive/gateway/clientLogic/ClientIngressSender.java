package com.weareadaptive.gateway.clientLogic;

import com.weareadaptive.oms.util.Side;
import com.weareadaptive.sbe.BufferUtils;
import com.weareadaptive.sbe.EncodeResult;

import org.agrona.DirectBuffer;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.Publication;
import io.aeron.cluster.client.AeronCluster;

/**
 * Listener logic for Egress messages from the cluster
 */
public class ClientIngressSender
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientIngressSender.class);
    private static final long RETRY_COUNT = 10;
    private final AeronCluster aeronCluster;
    private final IdleStrategy idleStrategy = new BusySpinIdleStrategy();
    private final PendingMessageManager pendingMessageManager;

    /**
     * Initialise class with aeronCluster
     *
     * @param aeronCluster - Cluster to offer ingress
     */
    public ClientIngressSender(final AeronCluster aeronCluster, final PendingMessageManager pendingMessageManager)
    {
        this.aeronCluster = aeronCluster;
        this.pendingMessageManager = pendingMessageManager;
    }

    /**
     * * Send Place Order Ingress binary encoding to cluster
     * - Encode buffer
     * - Offer Ingress to cluster
     *
     * @param correlationId - Ingress correlationId
     * @param price       - Ingress order price
     * @param size        - Ingress order size
     * @param side        - Ingress order side
     */
    public void placeOrder(
        final long correlationId,
        final double price,
        final long size,
        final Side side
    )
    {
        final EncodeResult encodeResult = BufferUtils.I_PO_ENCODER(correlationId, price, size, side);
        pendingMessageManager.addMessage(correlationId, "place-order");
        retryingOfferToCluster(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength());
    }

    /**
     * * Send Cancel Order Ingress binary encoding to cluster
     * - Encode buffer
     * - Offer Ingress to cluster
     *
     * @param correlationId - Ingress correlationId
     * @param orderId     - Ingress orderID to cancel
     */
    public void cancelOrder(
        final long correlationId,
        final long orderId
    )
    {
        final EncodeResult encodeResult = BufferUtils.I_CO_Encoder(correlationId, orderId);
        pendingMessageManager.addMessage(correlationId, "cancel-order");
        retryingOfferToCluster(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength());
    }

    /**
     * * Send Clear Orderbook Ingress binary encoding to cluster
     * - Encode buffer
     * - Offer Ingress to cluster
     *
     * @param correlationId - Ingress correlationId
     */
    public void clearOrderbook(final long correlationId)
    {
        final EncodeResult encodeResult = BufferUtils.I_CLEAR_Encoder(correlationId);
        pendingMessageManager.addMessage(correlationId, "clear-orderbook");
        retryingOfferToCluster(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength());
    }

    /**
     * * Send Reset Orderbook Ingress binary encoding to cluster
     * - Encode buffer
     * - Offer Ingress to cluster
     *
     * @param correlationId - Ingress correlationId
     */
    public void resetOrderbook(final long correlationId)
    {
        final EncodeResult encodeResult = BufferUtils.I_RESET_Encoder(correlationId);
        pendingMessageManager.addMessage(correlationId, "reset-orderbook");
        retryingOfferToCluster(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength());
    }

    private void retryingOfferToCluster(final DirectBuffer buffer, final int offset, final int length)
    {
        int retries = 0;
        do
        {
            final long result = aeronCluster.offer(buffer, offset, length);
            if (result > 0L)
            {
                return;
            }
            else if (result == Publication.ADMIN_ACTION || result == Publication.BACK_PRESSURED)
            {
                LOGGER.info("backpressure or admin action on cluster offer");
            }
            else if (result == Publication.NOT_CONNECTED || result == Publication.MAX_POSITION_EXCEEDED)
            {
                LOGGER.info("Cluster is not connected, or maximum position has been exceeded. Message lost.");
                return;
            }

            idleStrategy.idle();
            retries += 1;
            LOGGER.info("failed to send message to cluster. Retrying (" + retries + " of " + RETRY_COUNT + ")");
        }
        while (retries < RETRY_COUNT);

        LOGGER.info("Failed to send message to cluster. Message lost.");
    }
}
