package com.weareadaptive.oms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import com.weareadaptive.cluster.services.oms.util.Side;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxTestContext;

public class WebsocketTest
{
    static VertxTestContext testContext;
    Deployment deployment;
    Vertx vertx;
    boolean responseReceived = false;

    @BeforeEach
    void startup() throws InterruptedException
    {
        deployment = new Deployment();
        deployment.startCluster();
        deployment.getNodes().forEach((id, node) -> assertTrue(node.isActive()));
        deployment.startGateway();

        vertx = Vertx.vertx();
        testContext = new VertxTestContext();
        responseReceived = false;
    }

    @AfterEach
    void teardown() throws InterruptedException
    {
        deployment.shutdownCluster();
        deployment.shutdownGateway();
        vertx.close();
        responseReceived = false;
    }


    @Test
    public void writeRestingOrderToServer()
    {
        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {

            JsonObject order = createJsonPlaceOrder(Side.BID, 90, 10);
            ws.result().writeTextMessage(order.encode());

            ws.result().handler(buffer ->
            {
                responseReceived = true;
                testContext.verify(() -> assertEquals(1, buffer.toJsonObject().getLong("OrderId")));
                testContext.verify(() -> assertEquals("RESTING", buffer.toJsonObject().getString("Status")));
                testContext.completeNow();
            });
        });

        awaitResponse();
        assertTrue(testContext.completed());
    }

    @Test
    public void writePartialOrderToServer()
    {
        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {
            JsonObject order1 = createJsonPlaceOrder(Side.BID, 90, 10);
            ws.result().writeTextMessage(order1.encode());
        });

        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {

            JsonObject order2 = createJsonPlaceOrder(Side.ASK, 90, 15);
            ws.result().writeTextMessage(order2.encode());

            ws.result().handler(buffer ->
            {
                responseReceived = true;
                testContext.verify(() -> assertEquals(2, buffer.toJsonObject().getLong("OrderId")));
                testContext.verify(() -> assertEquals("PARTIAL", buffer.toJsonObject().getString("Status")));
                testContext.completeNow();
            });
        });

        awaitResponse();
        assertTrue(testContext.completed());
    }

    @Test
    public void writeFilledOrderToServer()
    {
        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {
            JsonObject order1 = createJsonPlaceOrder(Side.BID, 90, 10);
            ws.result().writeTextMessage(order1.encode());
        });

        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {

            JsonObject order2 = createJsonPlaceOrder(Side.ASK, 90, 10);
            ws.result().writeTextMessage(order2.encode());

            ws.result().handler(buffer ->
            {
                responseReceived = true;
                testContext.verify(() -> assertEquals(2, buffer.toJsonObject().getLong("OrderId")));
                testContext.verify(() -> assertEquals("FILLED", buffer.toJsonObject().getString("Status")));
                testContext.completeNow();
            });
        });

        awaitResponse();
        assertTrue(testContext.completed());
    }

    @Test
    public void cancelExistingOrder()
    {
        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {
            JsonObject order1 = createJsonPlaceOrder(Side.BID, 90, 10);
            ws.result().writeTextMessage(order1.encode());
        });

        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {

            JsonObject cancel = createJsonCancelOrder(1);
            ws.result().writeTextMessage(cancel.encode());

            ws.result().handler(buffer ->
            {
                responseReceived = true;
                testContext.verify(() -> assertEquals(1, buffer.toJsonObject().getLong("OrderId")));
                testContext.verify(() -> assertEquals("CANCELLED", buffer.toJsonObject().getString("Status")));
                testContext.completeNow();
            });
        });

        awaitResponse();
        assertTrue(testContext.completed());
    }

    @Test
    public void cancelNoneExistingOrder()
    {
        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {

            JsonObject cancel = createJsonCancelOrder(1);
            ws.result().writeTextMessage(cancel.encode());

            ws.result().handler(buffer ->
            {
                responseReceived = true;
                testContext.verify(() -> assertEquals(1, buffer.toJsonObject().getLong("OrderId")));
                testContext.verify(() -> assertEquals("NONE", buffer.toJsonObject().getString("Status")));
                testContext.completeNow();
            });
        });

        awaitResponse();
        assertTrue(testContext.completed());
    }

    @Test
    public void clearOrderbook()
    {
        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {

            JsonObject request = new JsonObject();
            request.put("command", "clear");
            ws.result().writeTextMessage(request.encode());

            ws.result().handler(buffer ->
            {
                responseReceived = true;
                testContext.verify(() -> assertEquals("SUCCESS", buffer.toJsonObject().getString("Status")));
                testContext.completeNow();
            });
        });

        awaitResponse();
        assertTrue(testContext.completed());
    }

    @Test
    public void resetOrderbook()
    {
        vertx.createHttpClient().webSocket(8080, "localhost", "/", ws ->
        {

            JsonObject request = new JsonObject();
            request.put("command", "reset");
            ws.result().writeTextMessage(request.encode());

            ws.result().handler(buffer ->
            {
                responseReceived = true;
                testContext.verify(() -> assertEquals("SUCCESS", buffer.toJsonObject().getString("Status")));
                testContext.completeNow();
            });
        });

        awaitResponse();
        assertTrue(testContext.completed());
    }


    private void awaitResponse()
    {
        final int TIMEOUT_LIMIT = 500;
        int TIMEOUT_COUNTER = 0;
        while (!responseReceived)
        {
            if (TIMEOUT_COUNTER == TIMEOUT_LIMIT)
            {
                break;
            }
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
            TIMEOUT_COUNTER++;
        }
    }

    private JsonObject createJsonPlaceOrder(final Side side, final long price, final long size)
    {
        JsonObject request = new JsonObject();
        request.put("command", "place");
        JsonObject payload = new JsonObject();
        payload.put("side", side.toString());
        payload.put("price", price);
        payload.put("size", size);
        request.put("payload", payload);
        return request;
    }

    private JsonObject createJsonCancelOrder(final long orderId)
    {
        JsonObject request = new JsonObject();
        request.put("command", "cancel");
        JsonObject payload = new JsonObject();
        payload.put("orderId", orderId);
        request.put("payload", payload);
        return request;
    }

}
