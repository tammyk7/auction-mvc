package com.weareadaptive.cluster;

import static com.weareadaptive.util.SetupConfigUtils.localHost;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import com.weareadaptive.cluster.clusterUtil.ClientSessionManager;
import com.weareadaptive.cluster.clusterUtil.SessionMessageContext;
import com.weareadaptive.cluster.clusterUtil.SnapshotManager;
import com.weareadaptive.cluster.services.OMSService;
import com.weareadaptive.sbe.CancelOrderIngressDecoder;
import com.weareadaptive.sbe.ClearOrderbookIngressDecoder;
import com.weareadaptive.sbe.MessageHeaderDecoder;
import com.weareadaptive.sbe.OrderIngressDecoder;
import com.weareadaptive.sbe.ResetOrderbookIngressDecoder;

import org.agrona.DirectBuffer;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.cluster.codecs.CloseReason;
import io.aeron.cluster.service.ClientSession;
import io.aeron.cluster.service.Cluster;
import io.aeron.cluster.service.ClusteredService;
import io.aeron.logbuffer.Header;

/**
 * Cluster Service Logic
 */
public class ClusterService implements ClusteredService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusteredService.class);
    private final IdleStrategy idleStrategy = new BusySpinIdleStrategy();
    private final ClientSessionManager clientSessionManager = new ClientSessionManager();
    private final SessionMessageContext sessionMessageContext = new SessionMessageContext(clientSessionManager);
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private Cluster cluster;
    private boolean isLeader = false;
    private SnapshotManager snapshotManager;
    private OMSService omsService;
    private int currentLeader = -1;
    private ExclusivePublication mdcPublication;

    /**
     * * Logic executed on cluster start
     * - Register services containing business logic and state
     * - Restore state in business logic using snapshot
     */
    @Override
    public void onStart(final Cluster cluster, final Image snapshotImage)
    {
        this.cluster = cluster;

        sessionMessageContext.setIdleStrategy(idleStrategy);
        registerOMSService();
        snapshotManager = new SnapshotManager(omsService.getOrderbook(), idleStrategy);
        if (null != snapshotImage)
        {
            try
            {
                restoreSnapshot(snapshotImage);
            }
            catch (final IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    private void registerMDCPublication(final Cluster cluster)
    {
        //Multi-Destination-Cast
        final String host = localHost("localhost");
        final int controlChannelPort = 40457;
        final var publicationChannel = "aeron:udp?control-mode=dynamic|control=" + host + ":" + controlChannelPort;
        mdcPublication = cluster.context().aeron().addExclusivePublication(publicationChannel, 100);
    }

    /**
     * * When a cluster client has connected to the cluster
     */
    @Override
    public void onSessionOpen(final ClientSession session, final long timestamp)
    {
        LOGGER.info("Client ID: " + session.id() + " Connected");
        sessionMessageContext.setClusterTime(timestamp);
        clientSessionManager.addSession(session);
    }

    /**
     * * When a cluster client has disconnected to the cluster
     */
    @Override
    public void onSessionClose(final ClientSession session, final long timestamp, final CloseReason closeReason)
    {
        LOGGER.info("Client ID: " + session.id() + " Disconnected");
        clientSessionManager.removeSession(session);
    }

    /**
     * * When the cluster has received Ingress from a cluster client
     */
    @Override
    public void onSessionMessage(
        final ClientSession session,
        final long timestamp,
        final DirectBuffer buffer,
        final int offset,
        final int length,
        final Header header
    )
    {
        sessionMessageContext.setSessionContext(session, timestamp);
        headerDecoder.wrap(buffer, offset);
        final long correlationId = headerDecoder.correlationId();
        switch (headerDecoder.templateId())
        {
            case OrderIngressDecoder.TEMPLATE_ID -> omsService.placeOrder(session, correlationId, buffer, offset);
            case CancelOrderIngressDecoder.TEMPLATE_ID -> omsService.cancelOrder(session, correlationId, buffer, offset);
            case ClearOrderbookIngressDecoder.TEMPLATE_ID -> omsService.clearOrderbook(session, correlationId, buffer, offset);
            case ResetOrderbookIngressDecoder.TEMPLATE_ID -> omsService.resetOrderbook(session, correlationId, buffer, offset);
        }

        if (isLeader)
        {
            //Broadcast 1 to all MDC subscriptions (clients that have subscribed)
            final UnsafeBuffer mdcBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(8));
            mdcBuffer.putLong(0, 1);
            mdcPublication.offer(mdcBuffer, 0, Long.BYTES);
        }

    }

    /**
     * * Orderbook state should be snapshotted for restoring state on cluster start.
     * - Convert data into binary encodings
     * - Offer to snapshotPublication
     */
    @Override
    public void onTakeSnapshot(final ExclusivePublication snapshotPublication)
    {
        snapshotManager.takeSnapshot(snapshotPublication);
    }

    /**
     * Orderbook state should be restored from snapshotImage on cluster start.
     * - Convert binary encodings into data
     * - Use data to restore Orderbook state
     *
     * @param snapshotImage snapshotImage to restore
     */
    public void restoreSnapshot(final Image snapshotImage) throws IOException, ClassNotFoundException
    {
        snapshotManager.loadSnapshot(snapshotImage);
    }

    @Override
    public void onTimerEvent(final long correlationId, final long timestamp)
    {
        sessionMessageContext.setClusterTime(timestamp);
    }

    /**
     * * When the cluster node changes role on election
     */
    @Override
    public void onRoleChange(final Cluster.Role newRole)
    {
        //Ensure MDC publication is running only on Leader
        if (Cluster.Role.LEADER == newRole)
        {
            isLeader = true;
            registerMDCPublication(cluster);
            LOGGER.info("MDC Publication Established");
        }
        else if (mdcPublication != null)
        {
            mdcPublication.close();
            mdcPublication = null;
            isLeader = false;
            LOGGER.info("MDC Publication Closed");
        }
        else
        {
            isLeader = false;
        }
    }

    @Override
    public void onNewLeadershipTermEvent(
        final long leadershipTermId,
        final long logPosition,
        final long timestamp,
        final long termBaseLogPosition,
        final int leaderMemberId,
        final int logSessionId,
        final TimeUnit timeUnit,
        final int appVersion)
    {
        LOGGER.info("Cluster node " + leaderMemberId + " is now Leader, previous Leader: " + currentLeader);
        currentLeader = leaderMemberId;
    }

    /**
     * * When the cluster node is terminating
     */
    @Override
    public void onTerminate(final Cluster cluster)
    {
        LOGGER.info("Cluster node is terminating");
    }

    private void registerOMSService()
    {
        omsService = new OMSService(sessionMessageContext);
    }

    /**
     * @return current cluster leader ID
     */
    public int getCurrentLeader()
    {
        return this.currentLeader;
    }

}
