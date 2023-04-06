package com.weareadaptive.grpcchat.receiver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.google.protobuf.Empty;
import com.wearadaptive.grpcchat.receiver.ChatService;
import com.weareadaptive.grpcchat.ChatServiceGrpc;
import com.weareadaptive.grpcchat.ChatServiceGrpc.ChatServiceStub;
import com.weareadaptive.grpcchat.ChatServiceOuterClass;
import com.weareadaptive.grpcchat.ChatServiceOuterClass.ChatMessageRequest;
import com.weareadaptive.grpcchat.ChatServiceOuterClass.ChatRoomEvent;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/*
  Thanks Shaun and ChatGPT 4!
 */
public class ChatServiceTest {

  private ChatService chatService;
  private StreamObserver<ChatRoomEvent> responseObserver;

  private ChatServiceStub asyncStub;

  private Server server;
  private ManagedChannel channel;


  @BeforeEach
  public void setUp() throws IOException {
    chatService = new ChatService();
    responseObserver = mock(StreamObserver.class);

    String serverName = InProcessServerBuilder.generateName();
    server = InProcessServerBuilder.forName(serverName).directExecutor().addService(chatService).build().start();
    channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();

    asyncStub = ChatServiceGrpc.newStub(channel);
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
  public void testSendChatMessage() {
    var request = createRequest();

    asyncStub.sendChatMessage(request, responseObserver);

    var eventCaptor = ArgumentCaptor.forClass(ChatRoomEvent.class);
    verify(responseObserver, times(1)).onNext(eventCaptor.capture());
    verify(responseObserver, times(1)).onCompleted();
    verifyNoMoreInteractions(responseObserver);

    ChatRoomEvent response = eventCaptor.getValue();
    assertEquals(request.getMessage(), response.getMessage());
  }

  @Test
  public void testSubscribeToChatMessages() {
    asyncStub.subscribeToChatMessages(Empty.getDefaultInstance(), responseObserver);
    asyncStub.sendChatMessage(createRequest(), responseObserver);

    ArgumentCaptor<ChatRoomEvent> eventCaptor = ArgumentCaptor.forClass(ChatRoomEvent.class);
    verify(responseObserver, times(2)).onNext(eventCaptor.capture());
    verify(responseObserver, times(1)).onCompleted();
    verifyNoMoreInteractions(responseObserver);

    var response = eventCaptor.getValue();
    var message = response.getMessage();
    assertEquals("test_user", message.getUser());
    assertEquals("test_message", message.getText());
  }

  private ChatMessageRequest createRequest() {
    ChatServiceOuterClass.Message message = ChatServiceOuterClass.Message.newBuilder().setUser("test_user").setText(
      "test_message").build();
    return ChatMessageRequest.newBuilder().setMessage(message).build();
  }
}

