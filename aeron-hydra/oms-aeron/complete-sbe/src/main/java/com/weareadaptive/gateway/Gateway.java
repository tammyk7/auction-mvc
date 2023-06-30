package com.weareadaptive.gateway;

import com.weareadaptive.gateway.agents.ClusterInteractionAgent;

import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gateway containing Aeron Client and Vertx Websocket Server
 */
public class Gateway
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Gateway.class);
    private static final IdleStrategy IDLE_STRATEGY = new BusySpinIdleStrategy();
    private ClusterInteractionAgent clusterInteractionAgent;

    /**
     * Method to start gateway
     *
     * @param maxNodes
     */
    public void startGateway(final int maxNodes)
    {
        LOGGER.info("Starting Gateway...");
        clusterInteractionAgent = new ClusterInteractionAgent(maxNodes);
        final AgentRunner clusterInteractionAgentRunner = new AgentRunner(IDLE_STRATEGY, Throwable::printStackTrace, null,
            clusterInteractionAgent);
        AgentRunner.startOnThread(clusterInteractionAgentRunner);
    }

    /**
     * @return current cluster leader ID
     */
    public int getLeaderId()
    {
        return clusterInteractionAgent.getLeaderId();
    }

    /**
     * @return boolean of isConnected to cluster
     */
    public boolean isConnectedToCluster()
    {
        return clusterInteractionAgent.isConnectedToCluster();
    }

    /**
     * Shutdown gateway
     */
    public void shutdown()
    {
        clusterInteractionAgent.shutdown();
    }
}
