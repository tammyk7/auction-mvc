package com.weareadaptive.client;

import com.weareadaptive.client.clientLogic.ClientIngressSender;
import com.weareadaptive.client.clientLogic.ClientEgressListener;
import io.aeron.cluster.client.AeronCluster;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import org.agrona.ErrorHandler;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static com.weareadaptive.util.ConfigUtils.egressChannel;
import static com.weareadaptive.util.ConfigUtils.ingressEndpoints;

/**
 * Aeron Client
 */
public class Client
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private MediaDriver mediaDriver;
    private static ClientEgressListener clientEgressListener;
    private static final IdleStrategy IDLE_STRATEGY = new BusySpinIdleStrategy();
    private static Thread keepClusterAlive;
    private static boolean isActive;
    private int initLeader = -1;

    /**
     * Method to start client
     * @param maxNodes max nodes
     */
    public void startAeronClient(final int maxNodes)
    {
        LOGGER.info("Starting Aeron Client...");
        clientEgressListener = new ClientEgressListener();

        mediaDriver = MediaDriver.launchEmbedded(new MediaDriver.Context()
            .threadingMode(ThreadingMode.DEDICATED)
            .dirDeleteOnStart(true)
            .dirDeleteOnShutdown(true)
        );
        try (
            AeronCluster aeronCluster = AeronCluster.connect(
                new AeronCluster.Context()
                .egressListener(clientEgressListener)
                .egressChannel(egressChannel())
                .aeronDirectoryName(mediaDriver.aeronDirectoryName())
                .ingressChannel("aeron:udp")
                .ingressEndpoints(ingressEndpoints(maxNodes))
                .isIngressExclusive(false)
                .messageTimeoutNs(TimeUnit.SECONDS.toNanos(10))
                .errorHandler(new ErrorHandler()
                {
                    @Override
                    public void onError(final Throwable throwable)
                    {
                        LOGGER.error(throwable.toString());
                        mediaDriver.close();
                    }
                })
            );
        )
        {
            LOGGER.info("Aeron Client connected to cluster successfully.");
            new ClientIngressSender(aeronCluster);
            isActive = true;
            initLeader = aeronCluster.leaderMemberId();

            /**
             * * A heartbeat loop to ensure the cluster client stays open
             *  - Without this, the client will close if it doesn't:
             *      - Send ingress
             *      - Receive egress
             */
            keepClusterAlive = new Thread(() ->
            {
                while (true)
                {
                    aeronCluster.sendKeepAlive();
                    LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
                }
            }
            );

            keepClusterAlive.start();

            while (isActive)
            {
                aeronCluster.pollEgress();
                IDLE_STRATEGY.idle();
            }
        }
    }

    /**
     * @return current cluster leader ID
     */
    public int getLeaderId()
    {
        return clientEgressListener.getCurrentLeader() == -1 ? initLeader : clientEgressListener.getCurrentLeader();
    }

    /**
     * Shutdown client
     */
    public void shutdown()
    {
        keepClusterAlive.interrupt();
        mediaDriver.close();
        isActive = false;
    }
}
