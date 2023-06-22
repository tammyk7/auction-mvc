package com.weareadaptive.grpc.client;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.greeting.GreetingRequest;
import proto.greeting.GreetingResponse;
import proto.greeting.GreetingServiceGrpc;

public class GreetingClient
{
    private static final Logger LOGGER = LoggerFactory.getThreadSafeLogger(GreetingClient.class);

    public static void main(final String[] args)
    {
        if (args.length == 0)
        {
            LOGGER.info("Need one argument").log();
            System.exit(1);
        }

        final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .build();

        if ("greet".equals(args[0]))
        {
            greet(channel);
        }
        else
        {
            LOGGER.info("Invalid input: ").append(args[0]).log();
        }

        LOGGER.info("Shutting down channel").log();
        channel.shutdown();
    }

    private static void greet(final ManagedChannel channel)
    {
        LOGGER.info("Entered greet function").log();
        final GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        final GreetingResponse response = stub.greet(GreetingRequest.newBuilder().setFirstName("Matias").build());

        LOGGER.info("Greeting: ").append(response.getResult()).log();
    }
}
