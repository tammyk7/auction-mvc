package com.example.websockets;

import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;

public class Main
{
    public static void main(String[] args)
    {

        Thread serverThread = new Thread(() ->
        {
            Vertx vertx = Vertx.vertx();
            vertx.deployVerticle(new WebSocketServer());
        });
        serverThread.start();

        Thread clientThread = new Thread(() ->
        {
            Vertx vertx = Vertx.vertx();
            // Simulating a POST request with payload "Knock Knock"
            vertx.createHttpClient().webSocket(8080, "localhost", "/", websocket ->
            {
                if (websocket.succeeded())
                {
                    WebSocket socket = websocket.result();
                    JsonObject message = new JsonObject()
                        .put("method", "POST")
                        .put("payload", "Knock Knock");

                    socket.writeTextMessage(message.toString());
                }
                else
                {
                    websocket.cause().printStackTrace();
                    vertx.close();
                }
            });

            // Simulating GET request
            vertx.createHttpClient().webSocket(8080, "localhost", "/", websocket ->
            {
                if (websocket.succeeded())
                {
                    WebSocket socket = websocket.result();
                    JsonObject message = new JsonObject()
                        .put("method", "GET");

                    socket.writeTextMessage(message.toString());
                }
                else
                {
                    websocket.cause().printStackTrace();
                    vertx.close();
                }
            });
        });
        clientThread.start();

    }
}
