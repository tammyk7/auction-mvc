import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.examples.chat.ChatMessage;
import io.grpc.examples.chat.ChatResponse;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.examples.chat.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class ServerMainTest
{

    private ChatServiceImpl chatService;
    private StreamObserver<ChatResponse> responseObserver;

    private ChatServiceGrpc.ChatServiceStub asyncStub;

    private Server server;
    private ManagedChannel channel;

    @BeforeEach
    public void setUp() throws IOException
    {
        chatService = new ChatServiceImpl();
        responseObserver = mock(StreamObserver.class);

        final String serverName = InProcessServerBuilder.generateName();

        server = InProcessServerBuilder.forName(serverName)
            .directExecutor()
            .addService(chatService.bindService())
            .build()
            .start();

        channel = InProcessChannelBuilder.forName(serverName)
            .directExecutor()
            .build();

        asyncStub = ChatServiceGrpc.newStub(channel);
    }

    @AfterEach
    public void cleanUp() throws Exception
    {
        if (server != null)
        {
            server.shutdownNow();
            server.awaitTermination();
        }
        if (channel != null)
        {
            channel.shutdownNow();
            channel.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    @Test
    public void testChatServiceCommunication() throws InterruptedException
    {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<ChatMessage> requestObserver = asyncStub.chatStream(new StreamObserver<ChatResponse>()
        {
            @Override
            public void onNext(ChatResponse response)
            {
                // Assert the response received from the server
                assertEquals("tammy: Wag1", response.getResponse());
                finishLatch.countDown();
            }

            @Override
            public void onError(Throwable t)
            {
            }

            @Override
            public void onCompleted()
            {
            }
        });

        // Send a message to the server
        final ChatMessage message = ChatMessage.newBuilder()
            .setUser("tammy")
            .setMessage("Wag1")
            .build();
        requestObserver.onNext(message);

        // Clean up: signal completion of the test
        requestObserver.onCompleted();
    }
}
