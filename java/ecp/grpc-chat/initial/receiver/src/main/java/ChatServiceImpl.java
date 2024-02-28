import io.grpc.examples.chat.ChatMessage;
import io.grpc.examples.chat.ChatResponse;
import io.grpc.examples.chat.ReactorChatServiceGrpc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;


public class ChatServiceImpl extends ReactorChatServiceGrpc.ChatServiceImplBase
{
    // This sink will hold and replay all chat messages.
    private final Sinks.Many<ChatMessage> chatSink = Sinks.many().replay().all();

    public ChatServiceImpl()
    {
    }

    @Override
    public Flux<ChatResponse> chatStream(Flux<ChatMessage> incomingMessages)
    {
        return Flux.merge(
            // Process incoming messages and add them to the chat history
            incomingMessages.doOnNext(message ->
                {
                    // This attempts to emit the message into the sink.
                    // If it fails (e.g., due to backpressure), it drops the message.
                    chatSink.tryEmitNext(message).orThrow();
                })
                .then()
                .thenMany(Flux.never()),
            // Emit chat history and new messages as ChatResponses
            chatSink.asFlux().map(message -> ChatResponse
                .newBuilder()
                .setResponse(message.getUser() + ": " + message.getMessage())
                .build())
        );
    }
}