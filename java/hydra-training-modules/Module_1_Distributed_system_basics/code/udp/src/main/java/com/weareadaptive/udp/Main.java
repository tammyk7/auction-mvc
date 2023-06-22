package com.weareadaptive.udp;

/**
 * This program exemplifies the fact that sending messages through UDP does not guarantee that messages will be received ordered.
 */
public class Main
{
    public static void main(final String[] args)
    {
        final Thread receiverThread = new Thread(() -> Receiver.main(new String[] {}));
        receiverThread.start();

        final Thread senderThread = new Thread(() -> Sender.main(new String[] {}));
        senderThread.start();
    }
}
