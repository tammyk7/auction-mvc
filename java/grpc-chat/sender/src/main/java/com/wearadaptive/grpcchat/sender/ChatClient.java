package com.wearadaptive.grpcchat.sender;

import com.google.protobuf.Empty;
import com.weareadaptive.grpcchat.ChatServiceGrpc;
import com.weareadaptive.grpcchat.ChatServiceOuterClass;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient {
    private static final Logger logger = Logger.getLogger(ChatClient.class.getName());


    final ChatServiceGrpc.ChatServiceBlockingStub blockingStub;
    final ChatServiceGrpc.ChatServiceStub asyncStub;
    final String user;


    public ChatClient(Channel channel, String user) {
        this.user = user;
        this.asyncStub = ChatServiceGrpc.newStub(channel);
        this.blockingStub =  ChatServiceGrpc.newBlockingStub(channel);

        this.subscribeToChats();
    }

    private void subscribeToChats() {

        StreamObserver<ChatServiceOuterClass.ChatRoomEvent> responseObserver = new StreamObserver<ChatServiceOuterClass.ChatRoomEvent>() {
            @Override
            public void onNext(ChatServiceOuterClass.ChatRoomEvent message) {
                logger.log(Level.INFO, "Got chat message from {0}: {1}",
                        new String[]{message.getMessage().getUser(),
                        message.getMessage().getText()});
            }

            @Override
            public void onError(Throwable t) {
                logger.log(Level.WARNING, "Chat messages failed: {0}", Status.fromThrowable(t));
            }

            @Override
            public void onCompleted() {
                logger.log(Level.INFO,"Finished chatting");
            }
        };

        this.asyncStub.subscribeToChatMessages(Empty.newBuilder().build(), responseObserver);
    }

    public void sendChatMessage(String message) {
        ChatServiceOuterClass.ChatMessageRequest messageRequest = ChatServiceOuterClass.ChatMessageRequest.newBuilder()
                .setMessage(ChatServiceOuterClass.Message.newBuilder().setText(message).setUser(user).build()).build();

        logger.log(Level.INFO, "Sending {0}", messageRequest);

        blockingStub.sendChatMessage(messageRequest);
    }


}
