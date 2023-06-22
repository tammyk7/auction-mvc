package com.weareadaptive.grpc;

import java.io.IOException;

import com.weareadaptive.grpc.client.GreetingClient;
import com.weareadaptive.grpc.server.GreetingServer;

public class Main
{
    public static void main(final String[] args)
    {
        final Thread serverThread = new Thread(() ->
        {
            try
            {
                GreetingServer.main(args);
            }
            catch (final IOException | InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();

        final Thread clientThread = new Thread(() -> GreetingClient.main(new String[] {"greet"}));
        clientThread.start();
    }
}
