package com.weareadaptive.chatroom.sender;

import com.weareadaptive.chatroom.entities.MutableBroadcastChatMessageRequest;
import com.weareadaptive.chatroom.sender.components.SenderCli;
import com.weareadaptive.chatroom.sender.components.SenderCliConnection;
import com.weareadaptive.chatroom.services.ChatRoomServiceProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

import java.util.Arrays;

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
        var user = args[0];
        var text = args[1];

        log.info("Creating connection to cluster").log();
        final SenderCliConnection connection = SenderCli.instance().run();
        final ChatRoomServiceProxy chatRoomService = connection.services().channelToCluster().getChatRoomServiceProxy();

        connection.services().lifecycle().onAvailable(() -> {
            log.info("Cluster has become available, sending chat message").log();
            try (final MutableBroadcastChatMessageRequest request = chatRoomService.acquireBroadcastChatMessageRequest()) {
                request.message().text(text);
                request.message().user(user);
                chatRoomService.broadcastMessage(chatRoomService.allocateCorrelationId(), request);
                log.info("Sent chat message to cluster: ").append(request).log();
                System.exit(0);   // A bit naughty but OK for what we're doing
            }
        });

        log.info("Connecting to cluster").log();
        connection.connect();
    }
}
