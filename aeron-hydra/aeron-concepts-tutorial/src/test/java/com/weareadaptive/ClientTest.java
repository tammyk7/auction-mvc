package com.weareadaptive;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientTest
{
    Deployment deployment;

    @BeforeEach
    void startup() {
        deployment = new Deployment();
    }

    @AfterEach
    void teardown() throws InterruptedException {
        deployment.shutdownCluster();
        deployment.shutdownClient();
    }

    @Test
    @DisplayName("A client is launched and connects to the cluster")
    void clientConnects() throws InterruptedException {
        deployment.startCluster();
        deployment.getNodes().forEach((id, node) -> assertTrue(node.isActive()));
        deployment.startClient();
        assertEquals(deployment.getClient().getLeaderId(),deployment.getLeaderId());
    }

}
