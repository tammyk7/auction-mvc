package com.weareadaptive.client;

import com.weareadaptive.client.agents.ClusterInteractionAgent;

import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gateway containing Aeron Client and Vertx Websocket Server
 */
public class Client
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private static final IdleStrategy IDLE_STRATEGY = new BusySpinIdleStrategy();
    private ClusterInteractionAgent clusterInteractionAgent;

    /**
     * Method to start client
     *
     * @param maxNodes
     */
    public void startClient(final int maxNodes)
    {
        LOGGER.info("Starting Basic Client...");
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
