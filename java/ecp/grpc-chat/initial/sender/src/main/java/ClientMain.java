import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.chat.ChatMessage;
import io.grpc.examples.chat.ReactorChatServiceGrpc;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;


import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientMain
{
    private final ReactorChatServiceGrpc.ReactorChatServiceStub asyncStub;
    private final ManagedChannel channel;

    public ClientMain(String target)
    {
        this.channel = ManagedChannelBuilder
            .forTarget(target)
            .usePlaintext()
            .build();
        this.asyncStub = ReactorChatServiceGrpc.newReactorStub(channel);
    }

    public void startChat(String username)
    {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("You can now start chatting! Type your message below:");

        // Create a Flux from user input, emitting on a separate thread to keep the application responsive
        final Flux<ChatMessage> messages = Flux.<ChatMessage>create(sink -> new Thread(() ->
            {
                while (scanner.hasNextLine())
                {
                    String message = scanner.nextLine();
                    if ("quit".equalsIgnoreCase(message))
                    {
                        sink.complete();
                        break;
                    }
                    sink.next(ChatMessage
                        .newBuilder()
                        .setUser(username)
                        .setMessage(message)
                        .build());
                }
            }).start())
            .publishOn(Schedulers.boundedElastic());

        // Subscribe to the Flux of ChatMessage and send it to the server
        asyncStub.chatStream(messages)
            .subscribe(
                response -> System.out.println(response.getResponse()),
                Throwable::printStackTrace
            );

        // Shutdown hook to cleanly close the channel
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            System.out.println("Chat ended by " + username);
            System.out.println("Shutting down client...");

            if (channel != null)
            {
                channel.shutdownNow();
                try
                {
                    channel.awaitTermination(5, TimeUnit.SECONDS);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
        }));
    }


    public static void main(String[] args)
    {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username:");
        String user = scanner.nextLine();
        String target = "localhost:8980";

        if (args.length > 0)
        {
            user = args[0];
        }
        if (args.length > 1)
        {
            target = args[1];
        }

        final ClientMain client = new ClientMain(target);
        client.startChat(user);
    }
}

