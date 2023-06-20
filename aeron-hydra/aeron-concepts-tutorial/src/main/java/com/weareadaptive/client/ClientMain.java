package com.weareadaptive.client;

public class ClientMain
{
    public static void main(String [] args)
    {
        int maxNodes = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        new Client().startAeronClient(maxNodes);
    }
}
