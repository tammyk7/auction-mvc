import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.grpc.examples.chat.ChatMessage;
import io.grpc.examples.chat.ChatResponse;
import io.grpc.examples.chat.ReactorChatServiceGrpc;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ChatServiceImplTest
{
    private ReactorChatServiceGrpc.ChatServiceImplBase chatService;

    @BeforeEach
    public void setUp()
    {
        chatService = new ChatServiceImpl();
    }

    @Test
    public void messageReceived()
    {
        final Flux<ChatMessage> incomingMessages = Flux.just(
            ChatMessage.newBuilder()
                .setUser("tammy")
                .setMessage("Wag1")
                .build()
        );

        final Flux<ChatResponse> responses = chatService.chatStream(incomingMessages);

        StepVerifier.create(responses)
            .expectNextMatches(response -> response
                .getResponse()
                .contains("Wag1"))
            .thenCancel()
            .verify();
    }
}
