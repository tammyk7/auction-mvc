package com.weareadaptive.chatroom.engine;

import com.weareadaptive.chatroom.entities.BroadcastChatMessageRequest;
import com.weareadaptive.chatroom.entities.MutableChatRoomEvent;
import com.weareadaptive.chatroom.services.ChatRoomService;
import com.weareadaptive.chatroom.services.ChatRoomServiceClientProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import com.weareadaptive.hydra.platform.core.TimeSource;

import org.agrona.concurrent.EpochClock;

/**
 * I am the implementation of the chat room service.
 * I live in the cluster
 * There is one instance of me (per cluster node) at runtime
 * I am single-threaded
 * I must be fast
 * I must be deterministic
 * Any state I maintain is derived as a result of replaying the ingress command log
 * Clients talk to me via {@link EngineChatRoomService}
 * I talk to clients via {@link ChatRoomServiceClientProxy}
 */
public class EngineChatRoomService implements ChatRoomService {

    private final Logger log = LoggerFactory.getNotThreadSafeLogger(EngineChatRoomService.class);
    private final ChatRoomServiceClientProxy clientProxy;
    private final TimeSource timeSource;


    public EngineChatRoomService(final ChatRoomServiceClientProxy clientProxy, final TimeSource timeSource) {
        this.clientProxy = clientProxy;
        this.timeSource = timeSource;
    }

    /**
     * Broadcast my chat message to anybody in this room. Clients use this service to
     * send messages to the room
     *
     * @param broadcastChatMessageRequest a message in the stream
     */
    @Override
    public void broadcastMessage(final UniqueId correlationId, final BroadcastChatMessageRequest broadcastChatMessageRequest) {
        log.info("Received request to broadcast message: ").append(broadcastChatMessageRequest).log();

        try (final MutableChatRoomEvent event = clientProxy.acquireChatRoomEvent()) {
            event.messageReceived().receivedTime(timeSource.nowMillis()).message().copyFrom(broadcastChatMessageRequest.message());
            log.info("Broadcasting event: ").append(event).log();
            clientProxy.onChatRoomEvent(event);
        }
    }
}
