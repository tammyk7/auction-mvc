package com.weareadaptive.oms;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GatewayTest
{
    Deployment deployment;

    @BeforeEach
    void startup()
    {
        deployment = new Deployment();
    }

    @AfterEach
    void teardown() throws InterruptedException
    {
        deployment.shutdownCluster();
        deployment.shutdownGateway();
    }

    @Test
    @DisplayName("A gateway is launched and connects to the cluster")
    void gatewayConnects() throws InterruptedException
    {
        deployment.startCluster();
        deployment.getNodes().forEach((id, node) -> assertTrue(node.isActive()));
        deployment.startGateway();
        assertTrue(deployment.getGateway().isConnectedToCluster());
    }

}
