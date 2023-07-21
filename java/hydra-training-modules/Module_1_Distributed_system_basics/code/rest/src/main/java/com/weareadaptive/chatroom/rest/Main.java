package com.weareadaptive.chatroom.rest;

import java.util.concurrent.CountDownLatch;

import com.weareadaptive.chatroom.rest.server.RestServer;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class Main
{
    private static final Logger LOGGER = LoggerFactory.getThreadSafeLogger(Main.class);

    public static void main(final String[] args)
    {
        final Vertx vertx = Vertx.vertx();
        final Thread serverThread = new Thread(() ->
        {
            vertx.deployVerticle(new RestServer());
        });
        serverThread.start();

        final Thread clientThread = new Thread(() ->
        {
            final CountDownLatch latch = new CountDownLatch(2);
            // Making a POST request
            final WebClient webClient = WebClient.create(vertx);
            final String payload = "Knock Knock";
            webClient.post(8080, "localhost", "/")
                .sendBuffer(Buffer.buffer(payload), ar ->
                {
                    if (ar.succeeded())
                    {
                        final HttpResponse<Buffer> response = ar.result();
                        LOGGER.info("POST request with message: ")
                            .append(payload)
                            .append(" replied with response Code: ")
                            .append(response.statusCode())
                            .log();
                    }
                    else
                    {
                        LOGGER.error("And exception has been thrown").append(ar.cause().getMessage()).log();
                    }
                    latch.countDown();
                });

            // Making a GET request
            webClient.get(8080, "localhost", "/")
                .send()
                .onComplete(r ->
                {
                    LOGGER.info("GET replied with response Code:")
                        .append(r.result().statusCode())
                        .log();
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
