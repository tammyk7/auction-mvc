package com.weareadaptive.gateway;

/**
 * Main class to launch gateway
 */
public class GatewayMain
{
    /**
     * main class
     * @param args arguments
     */
    public static void main(final String[] args)
    {
        final int maxNodes = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        new Gateway().startGateway(maxNodes);
    }
}
