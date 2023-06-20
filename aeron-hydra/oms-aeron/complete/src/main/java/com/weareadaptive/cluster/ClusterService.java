package com.weareadaptive.cluster;

import com.weareadaptive.cluster.services.OMSService;
import com.weareadaptive.util.BufferOffsets;
import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.cluster.client.AeronCluster;
import io.aeron.cluster.codecs.CloseReason;
import io.aeron.cluster.service.ClientSession;
import io.aeron.cluster.service.Cluster;
import io.aeron.cluster.service.ClusteredService;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class ClusterService implements ClusteredService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusteredService.class);
    private OMSService omsService;
    private Cluster cluster;

    private int currentLeader = -1;

    /**
     * * Logic executed on cluster start
     *      - Register services containing business logic and state
     *      - Restore state in business logic using snapshot
     */
    @Override
    public void onStart(Cluster cluster, Image snapshotImage)
    {
        this.cluster = cluster;
        registerOMSService();

        if (null != snapshotImage) {
            try
            {
                restoreSnapshot(snapshotImage);
            }
            catch (IOException | ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * * When a cluster client has connected to the cluster
     */
    @Override
    public void onSessionOpen(ClientSession session, long timestamp)
    {
        LOGGER.info("Client ID: " + session.id() + " Connected");
    }

    /**
     * * When a cluster client has disconnected to the cluster
     */
    @Override
    public void onSessionClose(ClientSession session, long timestamp, CloseReason closeReason)
    {
        LOGGER.info("Client ID: " + session.id() + " Disconnected");
    }

    /**
     * * When the cluster has received Ingress from a cluster client
     */
    @Override
    public void onSessionMessage(ClientSession session, long timestamp, DirectBuffer buffer, int offset, int length, Header header)
    {
        byte command = buffer.getByte(offset + BufferOffsets.COMMAND_OFFSET);
        long correlation = buffer.getLong(offset + BufferOffsets.CORRELATION_OFFSET);

        switch(command)
        {
            case 1 -> omsService.placeOrder(session, correlation, buffer, offset);
            case 2 -> omsService.cancelOrder(session, correlation, buffer, offset);
            case 3 -> omsService.clearOrderbook(session, correlation, buffer, offset);
            case 4 -> omsService.resetOrderbook(session, correlation, buffer, offset);
        }

    }

    /**
     * * Orderbook state should be snapshotted for restoring state on cluster start.
     *      - Convert data into binary encodings
     *      - Offer to snapshotPublication
     */
    @Override
    public void onTakeSnapshot(ExclusivePublication snapshotPublication)
    {
        try
        {
            omsService.onTakeSnapshot(snapshotPublication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * * Orderbook state should be restored from snapshotImage on cluster start.
     *      - Convert binary encodings into data
     *      - Use data to restore Orderbook state
     */
    public void restoreSnapshot(Image snapshotImage) throws IOException, ClassNotFoundException
    {
        omsService.onRestoreSnapshot(snapshotImage);
    }

    @Override
    public void onTimerEvent(long correlationId, long timestamp)
    {

    }

    /**
     * * When the cluster node changes role on election
     */
    @Override
    public void onRoleChange(Cluster.Role newRole)
    {
//        LOGGER.info("Cluster node " + cluster.memberId() + " is now: " + newRole);
    }

    @Override
    public void onNewLeadershipTermEvent(
            long leadershipTermId,
            long logPosition,
            long timestamp,
            long termBaseLogPosition,
            int leaderMemberId,
            int logSessionId,
            TimeUnit timeUnit,
            int appVersion)
    {
        LOGGER.info("Cluster node " + leaderMemberId + " is now Leader, previous Leader: " + currentLeader);
        currentLeader = leaderMemberId;
    }

    /**
     * * When the cluster node is terminating
     */
    @Override
    public void onTerminate(Cluster cluster)
    {
        LOGGER.info("Cluster node is terminating");
    }

    private void registerOMSService()
    {
        omsService = new OMSService();
    }

    public int getCurrentLeader() {
        return this.currentLeader;
    }

}
