package com.weareadaptive.oms.ws;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
public class WebsocketTest {

    private Vertx vertx;
    private HttpClient vertxClient;

    @BeforeEach
    void setUp(VertxTestContext testContext) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new WebSocketServer(), testContext.succeeding(id -> testContext.completeNow()));
        vertxClient = vertx.createHttpClient();
    }

    @AfterEach
    void tearDown() {
        vertxClient.close();
        vertx.close();
    }

    @Test
    @DisplayName("Establish connection from WS Client to WS Server")
    public void connectToServer(VertxTestContext testContext) throws InterruptedException {
        vertxClient.webSocket(8080, "localhost", "", client -> {
            testContext.completeNow();
        });
        assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Place Order request from client and receive response from server")
    public void wsPlaceOrderRequest(VertxTestContext testContext) throws Throwable {
        /**
         * * Sends websocket request to server to place order
         *   Should receive a response containing corresponding orderId and status
         */
        testContext.completeNow();
        assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Cancel Order request from client and receive response from server")
    public void wsCancelOrderRequest(VertxTestContext testContext) throws Throwable {
        /**
         * * Sends websocket request to server to cancel order
         *   Should receive a response containing corresponding orderId and status
         */
        testContext.completeNow();
        assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Clear Orderbook request from client and receive response from server")
    public void wsClearOrderbookRequest(VertxTestContext testContext) throws Throwable {
        /**
         * * Sends websocket request to server to clear Orderbook
         *   Should receive a response indicating success
         */
        testContext.completeNow();
        assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("Reset Orderbook request from client and receive response from server")
    public void wsResetOrderbookRequest(VertxTestContext testContext) throws Throwable {
        /**
         * * Sends websocket request to server to reset Orderbook
         *   Should receive a response indicating success
         */
        testContext.completeNow();
        assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }

}
