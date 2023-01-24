package com.wearadaptive.grpcchat.receiver;

import com.google.protobuf.Empty;
import com.weareadaptive.grpcchat.ChatServiceGrpc;
import com.weareadaptive.grpcchat.ChatServiceOuterClass;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {

    private final List<StreamObserver<ChatServiceOuterClass.ChatRoomEvent>> clients;

    public ChatService() {
        clients = new ArrayList<>();
    }

    @Override
    public void sendChatMessage(ChatServiceOuterClass.ChatMessageRequest request, StreamObserver<Empty> responseObserver) {
        ChatServiceOuterClass.ChatRoomEvent newEvent = ChatServiceOuterClass.ChatRoomEvent.newBuilder().setMessage(request.getMessage()).build();
        for (StreamObserver<ChatServiceOuterClass.ChatRoomEvent> client : clients) {
            client.onNext(newEvent);
        }
    }

    @Override
    public void subscribeToChatMessages(Empty request, StreamObserver<ChatServiceOuterClass.ChatRoomEvent> responseObserver) {
        clients.add(responseObserver);
    }
}
