package com.weareadaptive.cluster;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.weareadaptive.cluster.services.OMSService;
import com.weareadaptive.sbe.CancelOrderIngressDecoder;
import com.weareadaptive.sbe.ClearOrderbookIngressDecoder;
import com.weareadaptive.sbe.MessageHeaderDecoder;
import com.weareadaptive.sbe.OrderIngressDecoder;
import com.weareadaptive.sbe.ResetOrderbookIngressDecoder;

import org.agrona.DirectBuffer;
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
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private OMSService omsService;
    private int currentLeader = -1;

    /**
     * * Logic executed on cluster start
     * - Register services containing business logic and state
     * - Restore state in business logic using snapshot
     */
    @Override
    public void onStart(final Cluster cluster, final Image snapshotImage)
    {
        registerOMSService();

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

    /**
     * * When a cluster client has connected to the cluster
     */
    @Override
    public void onSessionOpen(final ClientSession session, final long timestamp)
    {
        LOGGER.info("Client ID: " + session.id() + " Connected");
    }

    /**
     * * When a cluster client has disconnected to the cluster
     */
    @Override
    public void onSessionClose(final ClientSession session, final long timestamp, final CloseReason closeReason)
    {
        LOGGER.info("Client ID: " + session.id() + " Disconnected");
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
        headerDecoder.wrap(buffer, offset);
        final long correlation = headerDecoder.correlation();
        switch (headerDecoder.templateId())
        {
            case OrderIngressDecoder.TEMPLATE_ID -> omsService.placeOrder(session, correlation, buffer, offset);
            case CancelOrderIngressDecoder.TEMPLATE_ID -> omsService.cancelOrder(session, correlation, buffer, offset);
            case ClearOrderbookIngressDecoder.TEMPLATE_ID -> omsService.clearOrderbook(session, correlation, buffer, offset);
            case ResetOrderbookIngressDecoder.TEMPLATE_ID -> omsService.resetOrderbook(session, correlation, buffer, offset);
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
        try
        {
            omsService.onTakeSnapshot(snapshotPublication);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
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
        omsService.onRestoreSnapshot(snapshotImage);
    }

    @Override
    public void onTimerEvent(final long correlationId, final long timestamp)
    {

    }

    /**
     * * When the cluster node changes role on election
     */
    @Override
    public void onRoleChange(final Cluster.Role newRole)
    {
//        LOGGER.info("Cluster node " + cluster.memberId() + " is now: " + newRole);
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
        omsService = new OMSService();
    }

    /**
     * @return current cluster leader ID
     */
    public int getCurrentLeader()
    {
        return this.currentLeader;
    }

}
