package com.weareadaptive.websockets.server;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

public class WebSocketServer extends AbstractVerticle
{
    private static final Logger LOGGER = LoggerFactory.getThreadSafeLogger(WebSocketServer.class);

    @Override
    public void start()
    {
        final HttpServer server = vertx.createHttpServer()
            .webSocketHandler(this::wsHandler);
        server.listen(8080);
    }

    private void wsHandler(final ServerWebSocket ws)
    {
        ws.handler(request ->
        {
            final String method = request.toJsonObject().getString("method");
            final String payload = request.toJsonObject().getString("payload");
            switch (method)
            {
                case "POST" -> LOGGER.info("POST request made with payload: ").append(payload).log();
                case "GET" -> LOGGER.info("GET request made").log();
                default -> throw new RuntimeException("Unsupported method: " + method);
            }
        });
    }
}
