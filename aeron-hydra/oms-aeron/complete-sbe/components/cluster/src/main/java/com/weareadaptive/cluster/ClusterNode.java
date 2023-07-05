package com.weareadaptive.cluster;

import static com.weareadaptive.util.SetupConfigUtils.awaitDnsResolution;
import static com.weareadaptive.util.SetupConfigUtils.getBasePort;
import static com.weareadaptive.util.SetupConfigUtils.getClusterAddresses;
import static com.weareadaptive.util.SetupConfigUtils.getMultiNodeClusterAddresses;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.agrona.CloseHelper;
import org.agrona.concurrent.ShutdownSignalBarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.CommonContext;
import io.aeron.cluster.ClusteredMediaDriver;
import io.aeron.cluster.service.ClusteredServiceContainer;
import io.aeron.samples.cluster.ClusterConfig;

/**
 * Cluster Node Start and Config
 */
public class ClusterNode
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterNode.class);
    private final ClusterService clusterService = new ClusterService();
    private boolean active = false;
    private ShutdownSignalBarrier barrier;

    /**
     * Method to start node
     *
     * @param node     nodeID
     * @param maxNodes maxiumum total cluster nodes
     * @param test     debug/testing bool
     */
    public void startNode(final int node, final int maxNodes, final boolean test)
    {
        LOGGER.info("Starting Cluster Node...");
        barrier = new ShutdownSignalBarrier();


        final int portBase = getBasePort();
        final int nodeId = node;
        final String hosts = maxNodes == 1 ? getClusterAddresses() : getMultiNodeClusterAddresses(maxNodes);

        //This may need tuning for your environment.
        final File baseDir = new File(System.getProperty("user.dir") + "/aeronCluster/", "node-" + nodeId);
        final String aeronDirName = CommonContext.getAeronDirectoryName() + "-" + nodeId + "-driver";

        LOGGER.info("Base Dir: " + baseDir);
        LOGGER.info("Aeron Dir: " + aeronDirName);

        final List<String> hostAddresses = List.of(hosts.split(","));
        final ClusterConfig clusterConfig = ClusterConfig.create(
            nodeId,
            hostAddresses,
            hostAddresses,
            portBase,
            clusterService
        );
        clusterConfig.consensusModuleContext().ingressChannel("aeron:udp");

        clusterConfig.archiveContext().archiveDir(new File(baseDir, "archive"));
        clusterConfig.clusteredServiceContext().clusterDir(new File(baseDir, "cluster"));
        clusterConfig.consensusModuleContext().clusterDir(new File(baseDir, "cluster"));
        clusterConfig.consensusModuleContext().deleteDirOnStart(test);
        clusterConfig.mediaDriverContext().aeronDirectoryName(aeronDirName);
        clusterConfig.mediaDriverContext().spiesSimulateConnection(true);
        clusterConfig.archiveContext().aeronDirectoryName(aeronDirName);
        clusterConfig.aeronArchiveContext().aeronDirectoryName(aeronDirName);
//        clusterConfig.consensusModuleContext().egressChannel(egressChannel());
        //This may need tuning for your environment.
        clusterConfig.consensusModuleContext().leaderHeartbeatTimeoutNs(TimeUnit.SECONDS.toNanos(1));

        awaitDnsResolution(hostAddresses, nodeId);

        try (
            ClusteredMediaDriver clusteredMediaDriver = ClusteredMediaDriver.launch(
                clusterConfig.mediaDriverContext(),
                clusterConfig.archiveContext(),
                clusterConfig.consensusModuleContext());
            ClusteredServiceContainer clusteredServiceContainer = ClusteredServiceContainer.launch(
                clusterConfig.clusteredServiceContext()))
        {

            LOGGER.info("Started Cluster Node...");
            setActive(true);

            barrier.await();

            CloseHelper.quietClose(clusteredMediaDriver);
            CloseHelper.quietClose(clusteredServiceContainer);

            setActive(false);
            LOGGER.info("Exiting");
        }
    }

    /**
     * @return active status of node
     */
    public boolean isActive()
    {
        return this.active;
    }

    /**
     * set active state of node
     *
     * @param active node active status boolean
     */
    public void setActive(final boolean active)
    {
        this.active = active;
    }

    /**
     * @return shutdown barrier for graceful node shutdown
     */
    public ShutdownSignalBarrier getBarrier()
    {
        return barrier;
    }

    /**
     * @return current leader nodeID
     */
    public int getLeaderId()
    {
        return clusterService.getCurrentLeader();
    }
}
