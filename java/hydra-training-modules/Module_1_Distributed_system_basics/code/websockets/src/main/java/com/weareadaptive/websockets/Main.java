package com.weareadaptive.websockets;

import java.util.concurrent.CountDownLatch;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.websockets.server.WebSocketServer;

import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;

public class Main
{
    private static final Logger LOGGER = LoggerFactory.getThreadSafeLogger(Main.class);
    public static void main(final String[] args)
    {
        final Vertx vertx = Vertx.vertx();
        final Thread serverThread = new Thread(() -> vertx.deployVerticle(new WebSocketServer()));
        serverThread.start();


        final CountDownLatch latch = new CountDownLatch(2);
        final Thread clientThread = new Thread(() ->
        {
            // Simulating a POST request with payload "Knock Knock"
            vertx.createHttpClient().webSocket(8080, "localhost", "/", websocket ->
            {
                if (websocket.succeeded())
                {
                    final WebSocket socket = websocket.result();
                    final JsonObject message = new JsonObject().put("method", "POST").put("payload", "Knock Knock");

                    socket.writeTextMessage(message.toString());
                }
                else
                {
                    LOGGER.error("Got an exception while sending a POST request: ").append(websocket.cause().getMessage()).log();
                }
                latch.countDown();
            });

            // Simulating GET request
            vertx.createHttpClient().webSocket(8080, "localhost", "/", websocket ->
            {
                if (websocket.succeeded())
                {
                    final WebSocket socket = websocket.result();
                    final JsonObject message = new JsonObject().put("method", "GET");

                    socket.writeTextMessage(message.toString());
                }
                else
                {
                    LOGGER.error("Got an exception while sending a GET request: ").append(websocket.cause().getMessage()).log();
                }
                latch.countDown();
            });

            try
            {
                latch.await();
            }
            catch (final InterruptedException e)
            {
                throw new RuntimeException(e);
            }
            vertx.close();
        });
        clientThread.start();
    }
}
