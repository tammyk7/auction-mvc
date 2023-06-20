package com.weareadaptive.cluster;

import io.aeron.ExclusivePublication;
import io.aeron.Image;
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

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class ClusterService implements ClusteredService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusteredService.class);
    private int messagesReceived = 0;
    private int currentLeader = -1;

    /**
     * * Logic executed on cluster start
     *      - Restore state in business logic if snapshot exists
     */
    @Override
    public void onStart(Cluster cluster, Image snapshotImage)
    {
        if (null != snapshotImage)
        {
            restoreSnapshot(snapshotImage);
        }
    }

    /**
     * * When a cluster client has connected to the cluster
     */
    @Override
    public void onSessionOpen(ClientSession session, long timestamp)
    {
        LOGGER.info("[Client-" + session.id() + "] Connected");
    }

    /**
     * * When a cluster client has disconnected to the cluster
     */
    @Override
    public void onSessionClose(ClientSession session, long timestamp, CloseReason closeReason)
    {
        LOGGER.info("[Client-" + session.id() + "] Disconnected");
    }

    /**
     * * When the cluster has received Ingress from a cluster client
     */
    @Override
    public void onSessionMessage(ClientSession session, long timestamp, DirectBuffer buffer, int offset, int length, Header header)
    {
        //Receive and decode buffer to receive 0 Echo
        int ingressMessage = buffer.getInt(offset);
        LOGGER.info("[Client-" + session.id() + "] Ingress Message | " +  ingressMessage);

        //Increment cluster state of total messages received
        messagesReceived++;

        //Encode a 1 int Echo to respond back to client
        MutableDirectBuffer msgBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(Integer.BYTES));
        msgBuffer.putInt(0, 1);

        //Offer to client
        while (session.offer(msgBuffer, 0, Integer.BYTES) < 0);
    }

    /**
     * * State should be snapshotted for restoring state on cluster start.
     *      - Convert data into binary encodings
     *      - Offer to snapshotPublication
     */
    @Override
    public void onTakeSnapshot(ExclusivePublication snapshotPublication)
    {
        //Create buffer for snapshot and place messagesReceived int into buffer
        MutableDirectBuffer snapshotBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(Integer.BYTES));
        snapshotBuffer.putInt(0, messagesReceived);

        //Offer snapshot buffer to snapshot publication
        snapshotPublication.offer(snapshotBuffer, 0, Integer.BYTES);

        LOGGER.info("Cluster has taken snapshot | Messages Received: " + messagesReceived);
    }

    /**
     * * State should be restored from snapshotImage on cluster start.
     *      - Convert binary encodings into data
     *      - Use data to restore state
     */
    public void restoreSnapshot(Image snapshotImage)
    {
        while (!snapshotImage.isEndOfStream())                                                       // (1)
        {
            //Because our snapshot is a stream of messages written to a Publication, we use the Image.poll method for extracting data from the snapshot.
            final int fragmentsPolled = snapshotImage.poll(
                    (bufferFragment, offset, length, header) -> // (2)
                    {
                        messagesReceived = bufferFragment.getInt(offset);
                    },
                    Integer.MAX_VALUE);
            break;
        }

        LOGGER.info("Cluster has restored snapshot | Messages Received: " + messagesReceived);
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
        LOGGER.info("Cluster node is now: " + newRole);
    }

    /**
     * * When the cluster node is terminating
     */
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

    public int getCurrentLeader() {
        return this.currentLeader;
    }
}
