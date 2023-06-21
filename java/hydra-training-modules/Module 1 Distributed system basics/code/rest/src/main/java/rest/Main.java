package rest;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class Main
{

    private static final Logger logger = LoggerFactory.getLogger("Client");

    public static void main(String[] args)
    {

        Thread serverThread = new Thread(() ->
        {
            Vertx vertx = Vertx.vertx();
            Router router = Router.router(vertx);
            vertx.deployVerticle(new RestServer(router));
        });
        serverThread.start();

        Thread clientThread = new Thread(() ->
        {
            Vertx vertx = Vertx.vertx();
            // Making a POST request
            WebClient webClient = WebClient.create(vertx);
            String payload = "Knock Knock";
            webClient.post(8080, "localhost", "/")
                .sendBuffer(Buffer.buffer(payload), ar ->
                {
                    if (ar.succeeded())
                    {
                        HttpResponse<Buffer> response = ar.result();
                        logger.info("POST request with message: " + payload + " replied with response Code: " + response.statusCode());
                    }
                    else
                    {
                        ar.cause().printStackTrace();
                    }
                });

            // Making a GET request
            webClient.get(8080, "localhost", "/")
                .send()
                .onComplete(r ->
                {
                    logger.info("GET replied with response Code:" + r.result().statusCode());
                });
        });
        clientThread.start();
    }

}
