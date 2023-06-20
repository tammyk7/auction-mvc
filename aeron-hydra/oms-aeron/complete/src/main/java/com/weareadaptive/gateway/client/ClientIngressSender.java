package com.weareadaptive.gateway.client;

import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.util.BufferOffsets;
import io.aeron.cluster.client.AeronCluster;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.weareadaptive.util.BufferOffsets.*;

public class ClientIngressSender
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientIngressSender.class);
    AeronCluster aeronCluster;
    private final IdleStrategy idleStrategy = new BusySpinIdleStrategy();
    public ClientIngressSender(AeronCluster aeronCluster)
    {
        this.aeronCluster = aeronCluster;
    }

    /**
     * * Send Place Order Ingress binary encoding to cluster
     *      - Encode buffer
     *      - Offer Ingress to cluster
     */
    public void PlaceOrder(final long correlation, final double price, final long size, final Side side)
    {
        UnsafeBuffer buffer = BufferOffsets.I_PO_Encoder(correlation, price, size, side);

        while (aeronCluster.offer(buffer, 0, I_PO_LENGTH) < 0)
        {
            idleStrategy.idle(aeronCluster.pollEgress());
        }
    }

    /**
     * * Send Cancel Order Ingress binary encoding to cluster
     *      - Encode buffer
     *      - Offer Ingress to cluster
     */
    public void CancelOrder(final long correlation, final long orderId)
    {
        UnsafeBuffer buffer = BufferOffsets.I_CO_Encoder(correlation, orderId);

        while (aeronCluster.offer(buffer, 0, I_CO_LENGTH) < 0)
        {
            idleStrategy.idle(aeronCluster.pollEgress());
        }
    }

    /**
     * * Send Clear Orderbook Ingress binary encoding to cluster
     *      - Encode buffer
     *      - Offer Ingress to cluster
     */
    public void ClearOrderbook(final long correlation)
    {
        UnsafeBuffer buffer = BufferOffsets.I_CLEAR_Encoder(correlation);

        while (aeronCluster.offer(buffer, 0, HEADER_LENGTH) < 0)
        {
            idleStrategy.idle(aeronCluster.pollEgress());
        }
    }

    /**
     * * Send Reset Orderbook Ingress binary encoding to cluster
     *      - Encode buffer
     *      - Offer Ingress to cluster
     */
    public void ResetOrderbook(final long correlation)
    {
        UnsafeBuffer buffer = BufferOffsets.I_RESET_Encoder(correlation);

        while (aeronCluster.offer(buffer, 0, HEADER_LENGTH) < 0)
        {
            idleStrategy.idle(aeronCluster.pollEgress());
        }
    }


}
