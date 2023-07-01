package com.weareadaptive.gateway.agents;

import static com.weareadaptive.util.SetupConfigUtils.egressChannel;
import static com.weareadaptive.util.SetupConfigUtils.ingressEndpoints;

import java.util.concurrent.TimeUnit;

import com.weareadaptive.gateway.clientLogic.ClientEgressListener;
import com.weareadaptive.gateway.clientLogic.ClientIngressSender;
import com.weareadaptive.gateway.clientLogic.PendingMessageManager;
import com.weareadaptive.gateway.ws.WebSocketServer;

import org.agrona.concurrent.Agent;
import org.agrona.concurrent.SystemEpochClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.cluster.client.AeronCluster;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import io.vertx.core.Vertx;

public class ClusterInteractionAgent implements Agent
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterInteractionAgent.class);
    private static final long HEARTBEAT_INTERVAL = 250;
    private static Vertx vertx;
    private final int maxNodes;
    private final PendingMessageManager pendingMessageManager;
    private long lastHeartbeatTime = Long.MIN_VALUE;
    private AeronCluster aeronCluster;
    private MediaDriver mediaDriver;
    private ClientEgressListener clientEgressListener;
    private ClientIngressSender clientIngressSender;
    private boolean connectedToCluster = false;

    public ClusterInteractionAgent(final int maxNodes)
    {
        this.maxNodes = maxNodes;
        this.pendingMessageManager = new PendingMessageManager(SystemEpochClock.INSTANCE);
    }


    @Override
    public int doWork()
    {
        //send cluster heartbeat roughly every 250ms
        final long now = SystemEpochClock.INSTANCE.time();
        if (now >= (lastHeartbeatTime + HEARTBEAT_INTERVAL))
        {
            lastHeartbeatTime = now;
            if (connectedToCluster)
            {
                aeronCluster.sendKeepAlive();
            }
        }

        //poll outbound messages from the cluster
        if (null != aeronCluster && !aeronCluster.isClosed())
        {
            aeronCluster.pollEgress();
        }

        //check for timed-out messages
        pendingMessageManager.doWork();

        //always sleep
        return 0;
    }

    @Override
    public String roleName()
    {
        return "cluster-interaction-agent";
    }

    /**
     * Connects to the cluster
     *
     * @param maxNodes maximum nodes in cluster
     */
    private void connectCluster(final int maxNodes)
    {
        clientEgressListener = new ClientEgressListener(pendingMessageManager);
        mediaDriver = MediaDriver.launch(new MediaDriver.Context()
            .threadingMode(ThreadingMode.SHARED)
            .dirDeleteOnStart(true)
            .errorHandler(this::logError)
            .dirDeleteOnShutdown(true));
        aeronCluster = AeronCluster.connect(
            new AeronCluster.Context()
                .egressListener(clientEgressListener)
                .egressChannel(egressChannel())
                .ingressChannel("aeron:udp")
                .ingressEndpoints(ingressEndpoints(maxNodes))
                .errorHandler(this::logError)
                .aeronDirectoryName(mediaDriver.aeronDirectoryName())
                .messageTimeoutNs(TimeUnit.SECONDS.toNanos(5)));
        connectedToCluster = true;
        clientIngressSender = new ClientIngressSender(aeronCluster, pendingMessageManager);
        LOGGER.info("Connected to cluster leader, node " + aeronCluster.leaderMemberId());
        startWSServer();
    }

    private void startWSServer()
    {
        vertx = Vertx.vertx();
        final WebSocketServer webSocketServer = new WebSocketServer(clientIngressSender, clientEgressListener);
        vertx.deployVerticle(webSocketServer);
        LOGGER.info("Websocket started...");
    }

    /**
     * Disconnects from the cluster
     */
    private void disconnectCluster()
    {
        clientEgressListener = null;
        if (vertx != null)
        {
            vertx.close();
        }
        if (aeronCluster != null)
        {
            aeronCluster.close();
        }
        if (mediaDriver != null)
        {
            mediaDriver.close();
        }
        connectedToCluster = false;
    }

    private void logError(final Throwable throwable)
    {
        LOGGER.info("Error: " + throwable.getMessage());
    }

    @Override
    public void onStart()
    {
        connectCluster(maxNodes);
    }

    @Override
    public void onClose()
    {
        disconnectCluster();
    }

    /**
     * @return current cluster leader ID
     */
    public int getLeaderId()
    {
        return clientEgressListener.getCurrentLeader() == -1 ? -1 : clientEgressListener.getCurrentLeader();
    }

    public boolean isConnectedToCluster()
    {
        return connectedToCluster;
    }

    /**
     * Shutdown gateway
     */
    public void shutdown()
    {
        disconnectCluster();
    }
}
