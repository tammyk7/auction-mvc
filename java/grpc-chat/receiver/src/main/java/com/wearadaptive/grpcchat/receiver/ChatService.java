package com.wearadaptive.grpcchat.receiver;

import com.google.protobuf.Empty;
import com.weareadaptive.grpcchat.ChatServiceGrpc;
import com.weareadaptive.grpcchat.ChatServiceOuterClass;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatService extends ChatServiceGrpc.ChatServiceImplBase {

    private static final Logger logger = Logger.getLogger(ChatService.class.getName());

    private final List<StreamObserver<ChatServiceOuterClass.ChatRoomEvent>> clients;

    public ChatService() {
        clients = new ArrayList<>();
    }

    @Override
    public void sendChatMessage(ChatServiceOuterClass.ChatMessageRequest request, StreamObserver<Empty> responseObserver) {
        logger.log(Level.INFO, "Got message {0} from user {1}", new String[]{request.getMessage().getText(), request.getMessage().getUser()});
        ChatServiceOuterClass.ChatRoomEvent newEvent = ChatServiceOuterClass.ChatRoomEvent.newBuilder().setMessage(request.getMessage()).build();
        for (StreamObserver<ChatServiceOuterClass.ChatRoomEvent> client : clients) {
            client.onNext(newEvent);
        }
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void subscribeToChatMessages(Empty request, StreamObserver<ChatServiceOuterClass.ChatRoomEvent> responseObserver) {
        logger.log(Level.INFO, "Subscription request");
        clients.add(responseObserver);
    }
}
