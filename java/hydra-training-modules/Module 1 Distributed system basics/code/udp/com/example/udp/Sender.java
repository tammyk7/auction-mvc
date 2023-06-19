package com.example.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender
{

    public static void main(String[] args)
    {
        try
        {
            InetAddress receiverAddress = InetAddress.getLocalHost();
            int receiverPort = 12345;
            DatagramSocket socket = new DatagramSocket();

            for (int i = 1; i <= 1000000; i++)
            {
                String message = String.valueOf(i);
                byte[] buffer = message.getBytes();

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress, receiverPort);
                socket.send(packet);
            }

            socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}