package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.greeting.GreetingRequest;
import proto.greeting.GreetingResponse;
import proto.greeting.GreetingServiceGrpc;

public class GreetingClient {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Need one argument");
            System.exit(1);
        }

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        switch (args[0]) {
            case "greet": greet(channel); break;
            default:
                System.out.println("Invalid input: " + args[0]);
        }

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    private static void greet(ManagedChannel channel) {
        System.out.println("Entered greet function");
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        GreetingResponse response = stub.greet(GreetingRequest.newBuilder().setFirstName("Matias").build());

        System.out.println("Greeting: " + response.getResult());
    }
}
