package com.wearadaptive.grpcchat.sender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.weareadaptive.grpcchat.ChatServiceGrpc.ChatServiceImplBase;
import com.weareadaptive.grpcchat.ChatServiceOuterClass.ChatMessageRequest;
import com.weareadaptive.grpcchat.ChatServiceOuterClass.ChatRoomEvent;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

public class ChatClientTest {

  private final ChatServiceImplBase serviceImpl =
      mock(ChatServiceImplBase.class, delegatesTo(
          new ChatServiceImplBase() {
            @Override
            public void sendChatMessage(ChatMessageRequest request,
                                        StreamObserver<ChatRoomEvent> responseObserver) {
              responseObserver.onNext(ChatRoomEvent.newBuilder().build());
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
    var requestCaptor = ArgumentCaptor.forClass(ChatMessageRequest.class);

    client.sendChatMessage("test");

    verify(serviceImpl).sendChatMessage(requestCaptor.capture(), ArgumentMatchers.any());

    var request = requestCaptor.getValue();
    assertEquals("testUser", request.getMessage().getUser());
    assertEquals("test", request.getMessage().getText());
    assertEquals(1, client.getTotalSentMessageCount());
  }

}
