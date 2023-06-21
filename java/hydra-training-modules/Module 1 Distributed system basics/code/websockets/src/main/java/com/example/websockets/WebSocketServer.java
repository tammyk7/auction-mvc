package com.example.websockets;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class WebSocketServer extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger("logger");

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer()
                .webSocketHandler(this::WSHandler);
        server.listen(8080);
    }

    private void WSHandler(ServerWebSocket ws) {
        ws.handler(request -> {
            String method = request.toJsonObject().getString("method");
            String payload = request.toJsonObject().getString("payload");
            switch (method) {
                case "POST": {
                    logger.info("POST request made with payload: " + payload );
                    break;
                }
                case "GET": {
                    logger.info("GET request made");
                }
            }
        });
    }
}
