package com.weareadaptive.chatroom.sender;

import java.util.Arrays;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

/**
 * I am the entry point for the Sender CLI.
 * I expect to be invoked like this:
 * SenderMain "leon jones" "Here is a message I want to broadcast to the room"
 * <p>
 * I am a client of the cluster, specifically the chat room service
 * When run, I send a message to the cluster
 * I don't care about any response, I am trusting!
 * I exit after I've sent my message
 */
public class SenderMain {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expect 2 args, user and text. Got: " + Arrays.toString(args));
        }

        final Logger log = LoggerFactory.getNotThreadSafeLogger(SenderMain.class);

    }
}
