package com.weareadaptive.grpcchat.receiver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.wearadaptive.grpcchat.receiver.ChatService;
import com.weareadaptive.grpcchat.ChatServiceOuterClass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.grpc.stub.StreamObserver;

/*
  Thanks Shaun and ChatGPT 4!
 */
public class ChatServiceTest {

  private ChatService chatService;
  private StreamObserver<ChatServiceOuterClass.ChatRoomEvent> responseObserver;

  @BeforeEach
  public void setUp() {
    chatService = new ChatService();
    responseObserver = mock(StreamObserver.class);
  }

  @Test
  public void testSendChatMessage() {

    ChatServiceOuterClass.ChatMessageRequest request = createRequest();

    chatService.sendChatMessage(request, responseObserver);

    ArgumentCaptor<ChatServiceOuterClass.ChatRoomEvent> eventCaptor =
      ArgumentCaptor.forClass(ChatServiceOuterClass.ChatRoomEvent.class);
    verify(responseObserver, times(1)).onNext(eventCaptor.capture());
    verify(responseObserver, times(1)).onCompleted();
    verifyNoMoreInteractions(responseObserver);

    ChatServiceOuterClass.ChatRoomEvent response = eventCaptor.getValue();
    assertEquals(request.getMessage(), response.getMessage());
  }

  @Test
  public void testSubscribeToChatMessages() {
    chatService.subscribeToChatMessages(com.google.protobuf.Empty.getDefaultInstance(), responseObserver);
    chatService.sendChatMessage(createRequest(), responseObserver);

    ArgumentCaptor<ChatServiceOuterClass.ChatRoomEvent> eventCaptor =
      ArgumentCaptor.forClass(ChatServiceOuterClass.ChatRoomEvent.class);
    verify(responseObserver, times(2)).onNext(eventCaptor.capture());
    verify(responseObserver, times(1)).onCompleted();
    verifyNoMoreInteractions(responseObserver);

    ChatServiceOuterClass.ChatRoomEvent response = eventCaptor.getValue();
    ChatServiceOuterClass.Message message = response.getMessage();
    assertEquals("test_user", message.getUser());
    assertEquals("test_message", message.getText());
  }

  private ChatServiceOuterClass.ChatMessageRequest createRequest() {
    ChatServiceOuterClass.Message message = ChatServiceOuterClass.Message.newBuilder().setUser("test_user").setText(
      "test_message").build();
    return ChatServiceOuterClass.ChatMessageRequest.newBuilder().setMessage(message).build();
  }
}

