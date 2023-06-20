package com.weareadaptive.cluster;

import io.aeron.Aeron;
import io.aeron.CommonContext;
import io.aeron.cluster.ClusteredMediaDriver;
import io.aeron.cluster.ConsensusModule;
import io.aeron.cluster.service.ClusteredServiceContainer;
import io.aeron.driver.MediaDriver;
import io.aeron.samples.cluster.ClusterConfig;
import org.agrona.concurrent.ShutdownSignalBarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.weareadaptive.util.ConfigUtils.*;

public class ClusterMain
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMain.class);

    /**
     * The main method.
     * @param args command line args
     */
    public static void main(final String[] args)
    {
        int nodeID = args.length > 0 ? Integer.parseInt(args[0]) : getClusterNode();
        int maxNodes = args.length > 0 ? Integer.parseInt(args[1]) : 1;
        boolean test = args.length > 0 ? Boolean.parseBoolean(args[2]) : false;
        LOGGER.info("Attempting to start cluster node: [NodeID: " + nodeID + "] | [MaxNodes: " + maxNodes + "] | [Test: " + test + "]" );
        new ClusterNode().startNode(nodeID, maxNodes, test);
    }

}
