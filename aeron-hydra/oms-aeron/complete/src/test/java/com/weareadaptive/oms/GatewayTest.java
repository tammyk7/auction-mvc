package com.weareadaptive.oms;

import com.weareadaptive.cluster.ClusterNode;
import com.weareadaptive.gateway.Gateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static org.junit.jupiter.api.Assertions.*;

public class GatewayTest
{
    Deployment deployment;

    @BeforeEach
    void startup() {
        deployment = new Deployment();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        deployment.shutdownCluster();
        deployment.shutdownGateway();
    }

    @Test
    @DisplayName("A gateway is launched and connects to the cluster")
    void gatewayConnects() throws InterruptedException {
        deployment.startCluster();
        deployment.getNodes().forEach((id, node) -> assertTrue(node.isActive()));
        deployment.startGateway();
        assertEquals(deployment.getGateway().getLeaderId(),deployment.getLeaderId());
    }

}
