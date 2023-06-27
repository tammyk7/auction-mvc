package com.weareadaptive.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.weareadaptive.util.ConfigUtils.getClusterNode;

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