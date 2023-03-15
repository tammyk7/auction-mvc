package com.wearadaptive.grpcchat.sender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;

import com.weareadaptive.grpcchat.ChatServiceGrpc;
import com.weareadaptive.grpcchat.ChatServiceOuterClass;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;

public class ChatClientTest {

  private final ChatServiceGrpc.ChatServiceImplBase serviceImpl =
    mock(ChatServiceGrpc.ChatServiceImplBase.class, delegatesTo(
      new ChatServiceGrpc.ChatServiceImplBase() {
        @Override
        public void sendChatMessage(ChatServiceOuterClass.ChatMessageRequest request,
                                    StreamObserver<ChatServiceOuterClass.ChatRoomEvent> responseObserver) {
          responseObserver.onNext(ChatServiceOuterClass.ChatRoomEvent.newBuilder().build());
          responseObserver.onCompleted();
        }
      }));
  private ChatClient client;
  private Server server;
  private ManagedChannel channel;

  @BeforeEach
  public void setUp() throws Exception {
    String serverName = InProcessServerBuilder.generateName();
    server = InProcessServerBuilder.forName(serverName).directExecutor().addService(serviceImpl).build().start();
    channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
    client = new ChatClient(channel, "testUser");
  }

  @AfterEach
  public void cleanUp() throws Exception {
    try {
      server.shutdown();
    } catch (Exception ex) {
      server.shutdownNow();
    }

    try {
      channel.shutdown();
    } catch (Exception ex) {
      channel.shutdownNow();
    }
  }

  @Test
  public void testWhenChatClientSendsAMessageItReceivesAMessageBack() {
    client.sendChatMessage("test");
    assertEquals(1, client.getTotalSentMessageCount());
  }

}
