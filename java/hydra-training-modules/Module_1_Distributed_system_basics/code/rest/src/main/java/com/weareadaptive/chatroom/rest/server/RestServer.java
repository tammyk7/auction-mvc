package com.weareadaptive.chatroom.rest.server;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;


public class RestServer extends AbstractVerticle
{
    private static final Logger LOGGER = LoggerFactory.getThreadSafeLogger(RestServer.class);

    @Override
    public void start()
    {
        final HttpServer server = vertx.createHttpServer();
        final Router router = Router.router(vertx);

        // Handles what happens when a request is made
        router.route().handler(ctx ->
        {
            final HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "text/plain");

            LOGGER.info("Got response: ").append(response.getStatusMessage()).log();

            response.end();
        });

        server.requestHandler(router).listen(8080);
    }
}
