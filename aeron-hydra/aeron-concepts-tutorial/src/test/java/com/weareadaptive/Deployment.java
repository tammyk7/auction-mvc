package com.weareadaptive;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.weareadaptive.client.Client;
import com.weareadaptive.cluster.ClusterNode;

public class Deployment
{

    private final HashMap<Integer, ClusterNode> nodes = new HashMap<>();
    private final HashMap<Integer, Thread> nodeThreads = new HashMap<>();
    private Client client;
    private Thread clientThread;

    public Client getClient()
    {
        return client;
    }

    public Thread getClientThread()
    {
        return clientThread;
    }

    public HashMap<Integer, ClusterNode> getNodes()
    {
        return nodes;
    }

    public HashMap<Integer, Thread> getNodeThreads()
    {
        return nodeThreads;
    }

    public void startSingleNodeCluster() throws InterruptedException
    {
        startNode(0, 1);
    }

    public void startCluster() throws InterruptedException
    {
        startNode(0, 3);
        startNode(1, 3);
        startNode(2, 3);
    }

    public void shutdownCluster()
    {
        nodes.forEach((id, node) ->
        {
            if (node != null && node.isActive())
            {
                // Signal the node to shutdown
                node.getBarrier().signal();
                // Wait for the node to shut down
                waitToDie(node);
            }
        });

        nodeThreads.forEach((id, thread) ->
        {
            if (thread != null)
            {
                // Interrupt the thread if it's still running
                thread.interrupt();
                // Join the thread
                try
                {
                    thread.join();
                }
                catch (final InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void startNode(final int nodeId, final int maxNodes) throws InterruptedException
    {
        final ClusterNode node = new ClusterNode();
        nodes.put(nodeId, node);

        final Thread nodeThread = new Thread(() ->
            node.startNode(nodeId, maxNodes, true)
        );

        nodeThreads.put(
            nodeId,
            nodeThread
        );
        nodeThread.start();
        waitToStart(node);
        nodeThread.join(2500);
    }

    public void stopNode(final int nodeId) throws InterruptedException
    {
        final ClusterNode node = nodes.get(nodeId);
        node.getBarrier().signal();
        waitToDie(node);
        nodeThreads.get(nodeId).interrupt();
        nodeThreads.get(nodeId).join();
        nodes.remove(nodeId);
        nodeThreads.remove(nodeId);
    }

    public void waitToStart(final ClusterNode node)
    {
        final int timeoutLimit = 50;
        int timeoutCounter = 0;
        while (!node.isActive())
        {
            if (timeoutCounter == timeoutLimit)
            {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
            timeoutCounter++;
        }
        assertTrue(node.isActive());
    }

    public void waitToDie(final ClusterNode node)
    {
        final int timeoutLimit = 50;
        int timeoutCounter = 0;
        while (node.isActive())
        {
            if (timeoutCounter == timeoutLimit)
            {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
            timeoutCounter++;
        }
        assertFalse(node.isActive());
    }

    public void waitForElection(final ClusterNode node)
    {
        final int timeoutLimit = 500;
        int timeoutCounter = 0;
        while (node.getLeaderId() == -1)
        {
            if (timeoutCounter == timeoutLimit)
            {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
            timeoutCounter++;
        }
    }

    public int findAliveNode()
    {
        return nodes.keySet().stream().findFirst().get();
    }

    public int getLeaderId()
    {
        return nodes.get(findAliveNode()).getLeaderId();
    }

    public boolean initiateLeadershipElection() throws InterruptedException
    {
        waitForElection(nodes.get(findAliveNode()));
        final int startingLeader = getLeaderId();
        stopNode(startingLeader);
        nodeThreads.get(findAliveNode()).join(2500);
        final int newLeader = getLeaderId();
        return startingLeader != newLeader;
    }

    void startClient() throws InterruptedException
    {
        client = new Client();

        clientThread = new Thread(() ->
            client.startAeronClient(3)
        );

        clientThread.start();
        clientThread.join(2500);
    }

    void shutdownClient() throws InterruptedException
    {
        client.shutdown();
        clientThread.interrupt();
        clientThread.join();
    }
}
