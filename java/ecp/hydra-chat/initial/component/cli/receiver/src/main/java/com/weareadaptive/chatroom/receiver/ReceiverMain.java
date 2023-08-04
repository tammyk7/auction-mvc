package com.weareadaptive.chatroom.receiver;

import java.util.Arrays;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

/**
 * I am a receiver. I expect one argument, which can be either
 * live
 * or
 * earliest
 * <p>
 * If you specify live, I will receive chat messages that are broadcast thereafter
 * If you specify earliest, I will receive historic chat messages and then any new ones
 * <p>
 * I connect to the cluster like any other imperative client
 * I establish a replay channel, register myself as a listener and join using the requested mechansim
 * I don't support subscribing from a specific position
 */
public class ReceiverMain {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Expect 1 arg, either 'live' or 'earliest'. Got: " + Arrays.toString(args));
        }
        final Logger log = LoggerFactory.getNotThreadSafeLogger(ReceiverMain.class);

        var joinAt = args[0];
        if (!("live".equals(joinAt) || "earliest".equals(joinAt))) {
            throw new IllegalArgumentException("Join At argument must be one of 'live' or 'earliest'. Got: " + joinAt);
        }

        log.info("Creating connection to cluster").log();

    }
}
