import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ServerMain
{
    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());
    private Server server;

    public void start() throws IOException
    {
        final int port = 8980;
        final ChatServiceImpl chatService = new ChatServiceImpl();

        server = ServerBuilder.forPort(port)
            .addService(chatService)
            .build()
            .start();


        logger.info("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            logger.info("*** shutting down gRPC server since JVM is shutting down");
            try
            {
                ServerMain.this.stop();
            }
            catch (InterruptedException e)
            {
                logger.severe("Server shutdown interrupted: " + e.getMessage());
            }
        }));
    }

    private void stop() throws InterruptedException
    {
        if (server != null)
        {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException
    {
        if (server != null)
        {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException
    {
        final ServerMain server = new ServerMain();
        server.start();
        server.blockUntilShutdown();
    }
}
