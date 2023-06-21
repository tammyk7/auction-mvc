package rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.io.IOException;


public class RestServer extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger("RestServer");
    private final Router router;

    public RestServer(Router router) {
        this.router = router;
    }

    @Override
    public void start() {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        // Handles what happens when a request is made
        router.route().handler(ctx -> {
            HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "text/plain");

            logger.info(ctx.response().getStatusMessage());

            response.end();
        });

        server.requestHandler(router).listen(8080);
    }
}