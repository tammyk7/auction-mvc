package com.wearadaptive.grpcchat.sender;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SenderMain {

    private final int port;
    private final ManagedChannel channel;
    private final ChatClient chatClient;
    private final ChatClient anotherClient;

    public SenderMain(int port) throws IOException {
        this.port = port;
        this.channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
        this.chatClient = new ChatClient(this.channel, "amity");
        this.anotherClient = new ChatClient(this.channel, "notamity");

    }

    public void sendSomeMessages() {
        this.chatClient.sendChatMessage("hi");
        this.chatClient.sendChatMessage("how");
        this.chatClient.sendChatMessage("are");
        this.chatClient.sendChatMessage("you?");
    }

    public void shutDown() throws InterruptedException {
        channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    }


    public static void main(String[] args) {
        try {
            SenderMain senderMain = new SenderMain(8980);
            senderMain.sendSomeMessages();
            senderMain.shutDown();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
