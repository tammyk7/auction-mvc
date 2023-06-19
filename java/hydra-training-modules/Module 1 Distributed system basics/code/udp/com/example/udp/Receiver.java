package com.example.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receiver
{
    public static void main(String[] args)
    {
        try
        {
            int receiverPort = 12345;
            DatagramSocket socket = new DatagramSocket(receiverPort);

            byte[] buffer = new byte[1024];

            int lastMessage = 0;

            while (true)
            {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                int value = Integer.valueOf(message);
                int expectedValue = lastMessage + 1;
                if (value != expectedValue)
                {
                    throw new Exception("unordered UDP packet, received:" + value + " instead of:" + expectedValue);
                }
                else
                {
                    lastMessage = value;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
