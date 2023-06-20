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


public class Client
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private static ClientEgressListener clientEgressListener;
    private static ClientIngressSender clientIngressSender;
    private static final IdleStrategy idleStrategy = new BusySpinIdleStrategy();
    private static Thread keepClusterAlive;
    private static boolean isActive;
    private int initLeader = -1;

    public void startAeronClient(int maxNodes)
    {
        LOGGER.info("Starting Aeron Client...");
        clientEgressListener = new ClientEgressListener();

        MediaDriver mediaDriver = MediaDriver.launchEmbedded(new MediaDriver.Context()
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
                            .messageTimeoutNs(TimeUnit.SECONDS.toNanos(5))
                            .errorHandler(new ErrorHandler() {
                                @Override
                                public void onError(Throwable throwable) {
                                    System.err.println(throwable);
                                }
                            })
            );
        )
        {
            LOGGER.info("Aeron Client connected to cluster successfully.");
            clientIngressSender = new ClientIngressSender(aeronCluster);
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
                    while (true) {
                        aeronCluster.sendKeepAlive();
                        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
                    }
                }
            );
            keepClusterAlive.start();

            while (isActive) {
                aeronCluster.pollEgress();
                idleStrategy.idle();
            }
        }
    }

    public int getLeaderId()
    {
        return clientEgressListener.getCurrentLeader() == -1 ? initLeader : clientEgressListener.getCurrentLeader();
    }

    public void shutdown()
    {
        keepClusterAlive.interrupt();
        isActive = false;
    }
}
