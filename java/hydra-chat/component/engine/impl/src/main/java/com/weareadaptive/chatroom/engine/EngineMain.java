package com.weareadaptive.chatroom.engine;

import com.weareadaptive.chatroom.engine.components.Engine;
import com.weareadaptive.chatroom.engine.components.EngineBootstrapper;
import com.weareadaptive.chatroom.engine.components.EngineContext;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.engine.bindings.ClusterNodeBinding;
import io.aeron.cluster.MillisecondClusterClock;

/**
 * I am the entry point for an instance of the engine (a single hydra cluster node)
 * I am called when the application starts, and I remain running until I'm killed
 * My responsibility is to wire together and bootstrap all the services I support
 */
public class EngineMain {
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(EngineMain.class);

    public static final EngineBootstrapper BOOTSTRAPPER = context ->
    {
        final var clientProxy = context.config().;
        final var service = new EngineEchoService(clientProxy);
        context.channelToClients().registerEchoService(service);
    };

    public static void main(final String[] args) {
        try (ClusterNodeBinding binding = Engine.node(BOOTSTRAPPER, new MillisecondClusterClock()).run()) {
            binding.awaitShutdown();
        }
    }
}
