package com.weareadaptive.grpc.server;

import java.io.IOException;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GreetingServer
{
    private static final Logger LOGGER = LoggerFactory.getThreadSafeLogger(GreetingServer.class);

    public static void main(final String[] args) throws IOException, InterruptedException
    {
        final int port = 50051;
        final Server server = ServerBuilder.forPort(port).addService(new GreetingServerImpl()).build();

        server.start();
        LOGGER.info("Server started").log();
        LOGGER.info("Listening on port: ").append(port).log();

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            LOGGER.info("Received shutdown request").log();
            server.shutdown();
            LOGGER.info("Server stopped").log();
        }));

        server.awaitTermination();
    }
}
