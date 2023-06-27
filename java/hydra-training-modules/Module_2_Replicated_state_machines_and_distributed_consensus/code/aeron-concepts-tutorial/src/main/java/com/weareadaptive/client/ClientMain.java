package com.weareadaptive.client;

/**
 * Main class to launch client
 */
public class ClientMain
{
    /**
     * main class
     * @param args arguments
     */
    public static void main(final String[] args)
    {
        final int maxNodes = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        new Client().startAeronClient(maxNodes);
    }
}
