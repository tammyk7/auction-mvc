package com.example.udp;

/**
 * This program exemplifies the fact that sending messages through UDP does not guarantee that messages will be received ordered.
 */
public class Main
{
    public static void main(String[] args)
    {
        Thread receiverThread = new Thread(() -> Receiver.main(new String[] {}));
        receiverThread.start();

        Thread senderThread = new Thread(() -> Sender.main(new String[] {}));
        senderThread.start();
    }
}
