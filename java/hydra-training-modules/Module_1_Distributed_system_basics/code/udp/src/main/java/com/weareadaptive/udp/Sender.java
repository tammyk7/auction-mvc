package com.weareadaptive.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

public class Sender
{
    private static final Logger LOGGER = LoggerFactory.getThreadSafeLogger(Sender.class);

    public static void main(final String[] args)
    {
        try
        {
            final InetAddress receiverAddress = InetAddress.getLocalHost();
            final int receiverPort = 12345;
            try (final DatagramSocket socket = new DatagramSocket())
            {

                for (int i = 1; i <= 1000000; i++)
                {
                    final String message = String.valueOf(i);
                    final byte[] buffer = message.getBytes();
                    final DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress, receiverPort);
                    socket.send(packet);
                }
            }
        }
        catch (final Exception e)
        {
            LOGGER.error("Failed to send UDP packet: ").append(e.getMessage()).log();
        }
    }
}
