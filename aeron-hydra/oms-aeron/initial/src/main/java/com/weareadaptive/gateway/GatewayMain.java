package com.weareadaptive.gateway;

public class GatewayMain
{
    public static void main(String [] args)
    {
        int maxNodes = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        new Gateway().startAeronClient(maxNodes);
    }
}
