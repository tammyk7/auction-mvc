package com.weareadaptive.client.clientLogic;

import io.aeron.cluster.client.AeronCluster;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Listener logic for Egress messages from the cluster
 */
public class ClientIngressSender
{

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientIngressSender.class);
    private final IdleStrategy idleStrategy = new SleepingIdleStrategy();
    AeronCluster aeronCluster;

    /**
     * Initialise class with aeronCluster
     * @param aeronCluster - Cluster to offer ingress
     */
    public ClientIngressSender(final AeronCluster aeronCluster)
    {
        this.aeronCluster = aeronCluster;

        //Offer a simple 0 Echo to the cluster on client start
        offerEcho();
    }

    private void offerEcho()
    {
        //Encode 0 int into a buffer
        final MutableDirectBuffer msgBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(Integer.BYTES));
        msgBuffer.putInt(0, 0);

        //Offer buffer with offset and buffer length
        while (aeronCluster.offer(msgBuffer, 0, Integer.BYTES) < 0)
        {
            idleStrategy.idle();
        }

        LOGGER.info("Ingress Message sent to cluster | " + 0);
    }
}
