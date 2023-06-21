import java.io.IOException;

import client.GreetingClient;
import server.GreetingServer;

public class Main
{
    public static void main(String[] args)
    {
        Thread serverThread = new Thread(() ->
        {
            try
            {
                GreetingServer.main(args);
            }
            catch (IOException | InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();

        Thread clientThread = new Thread(() -> GreetingClient.main(new String[] {"greet"}));
        clientThread.start();
    }
}
