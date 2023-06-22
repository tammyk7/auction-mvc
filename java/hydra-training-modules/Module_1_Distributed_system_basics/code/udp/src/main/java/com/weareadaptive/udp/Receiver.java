package com.weareadaptive.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

public class Receiver
{
    private static final Logger LOGGER = LoggerFactory.getThreadSafeLogger(Receiver.class);

    public static void main(final String[] args)
    {
        final int receiverPort = 12345;
        final byte[] buffer = new byte[1024];
        int lastMessage = 0;

        try (final DatagramSocket socket = new DatagramSocket(receiverPort))
        {
            while (true)
            {
                final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                final String message = new String(packet.getData(), 0, packet.getLength());
                final int value = Integer.parseInt(message);
                final int expectedValue = lastMessage + 1;
                if (value != expectedValue)
                {
                    LOGGER.error("unordered UDP packet, received: ").append(value).append(" instead of: ").append(expectedValue).log();
                    throw new IllegalStateException();
                }
                else
                {
                    lastMessage = value;
                }
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
