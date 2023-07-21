package com.weareadaptive.chatroom.engine;


import com.weareadaptive.chatroom.engine.components.Engine;
import com.weareadaptive.chatroom.engine.components.EngineBootstrapper;
import com.weareadaptive.chatroom.engine.components.EngineContext;
import com.weareadaptive.chatroom.engine.services.EngineChatRoomService;
import com.weareadaptive.chatroom.services.ChatRoomServiceClientProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.engine.bindings.ClusterNodeBinding;

import io.aeron.cluster.MillisecondClusterClock;

/**
 * I am the entry point for an instance of the engine (a single hydra cluster node)
 * I am called when the application starts, and I remain running until I'm killed
 * My responsibility is to wire together and bootstrap all the services I support
 */
public class EngineMain
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(EngineMain.class);
    public static final EngineBootstrapper BOOTSTRAPPER = context ->
    {
        LOGGER.info("Bootstrapping Chat Room Engine").log();
        registerChatRoomService(context);
    };

    /**
     * Create and register the chat room service.
     * The chat room service needs to send messages to the client, so it needs a chat room service client proxy
     */

    private static void registerChatRoomService(final EngineContext context)
    {
        // STEPS
        // 1. Get Client Proxy
        // 2. Create new instance of EngineChatRoomService
        // 3. Register ChatRoomService
        LOGGER.info("Registering Chat Room Service").log();
        // 1
        final ChatRoomServiceClientProxy clientProxy = context.channelToClients().getChatRoomServiceClientProxy();
        // 2
        final EngineChatRoomService service = new EngineChatRoomService(clientProxy);
        // 3
        context.channelToClients().registerChatRoomService(service);

        LOGGER.info("Registered Chat Room Service").log();
    }

    public static void main(final String[] args)
    {
        try (ClusterNodeBinding binding = Engine.node(BOOTSTRAPPER, new MillisecondClusterClock()).run())
        {
            binding.awaitShutdown();
        }
    }

}
