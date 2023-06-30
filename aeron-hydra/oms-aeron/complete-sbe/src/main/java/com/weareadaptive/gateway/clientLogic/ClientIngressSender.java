package com.weareadaptive.gateway.clientLogic;

import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.util.BufferUtils;
import com.weareadaptive.util.EncodeResult;

import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.cluster.client.AeronCluster;

/**
 * Listener logic for Egress messages from the cluster
 */
public class ClientIngressSender
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientIngressSender.class);
    private final AeronCluster aeronCluster;
    private final IdleStrategy idleStrategy = new BusySpinIdleStrategy();

    /**
     * Initialise class with aeronCluster
     *
     * @param aeronCluster - Cluster to offer ingress
     */
    public ClientIngressSender(final AeronCluster aeronCluster)
    {
        this.aeronCluster = aeronCluster;
    }

    /**
     * * Send Place Order Ingress binary encoding to cluster
     * - Encode buffer
     * - Offer Ingress to cluster
     *
     * @param correlation - Ingress correlationID
     * @param price       - Ingress order price
     * @param size        - Ingress order size
     * @param side        - Ingress order side
     */
    public void placeOrder(
        final long correlation,
        final double price,
        final long size,
        final Side side
    )
    {
        final EncodeResult encodeResult = BufferUtils.I_PO_ENCODER(correlation, price, size, side);

        while (aeronCluster.offer(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength()) < 0)
        {
            idleStrategy.idle(aeronCluster.pollEgress());
        }
    }

    /**
     * * Send Cancel Order Ingress binary encoding to cluster
     * - Encode buffer
     * - Offer Ingress to cluster
     *
     * @param correlation - Ingress correlationID
     * @param orderId     - Ingress orderID to cancel
     */
    public void cancelOrder(
        final long correlation,
        final long orderId
    )
    {
        final EncodeResult encodeResult = BufferUtils.I_CO_Encoder(correlation, orderId);

        while (aeronCluster.offer(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength()) < 0)
        {
            idleStrategy.idle(aeronCluster.pollEgress());
        }
    }

    /**
     * * Send Clear Orderbook Ingress binary encoding to cluster
     * - Encode buffer
     * - Offer Ingress to cluster
     *
     * @param correlation - Ingress correlationID
     */
    public void clearOrderbook(final long correlation)
    {
        final EncodeResult encodeResult = BufferUtils.I_CLEAR_Encoder(correlation);

        while (aeronCluster.offer(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength()) < 0)
        {
            idleStrategy.idle(aeronCluster.pollEgress());
        }
    }

    /**
     * * Send Reset Orderbook Ingress binary encoding to cluster
     * - Encode buffer
     * - Offer Ingress to cluster
     *
     * @param correlation - Ingress correlationID
     */
    public void resetOrderbook(final long correlation)
    {
        final EncodeResult encodeResult = BufferUtils.I_RESET_Encoder(correlation);

        while (aeronCluster.offer(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength()) < 0)
        {
            idleStrategy.idle(aeronCluster.pollEgress());
        }
    }


}
