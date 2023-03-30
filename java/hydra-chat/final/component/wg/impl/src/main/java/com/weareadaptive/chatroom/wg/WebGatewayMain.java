package com.weareadaptive.chatroom.wg;

import com.weareadaptive.chatroom.wg.components.WebGateway;
import com.weareadaptive.chatroom.wg.components.WebGatewayBootstrapper;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.config.Configurer;
import com.weareadaptive.hydra.platform.web.bindings.WebBinding;

/**
 * I am the entry point for an instance of the engine (a single hydra cluster node)
 * I am called when the application starts, and I remain running until I'm killed
 * My responsibility is to wire together and bootstrap all the services I support
 */
public class WebGatewayMain {
    private static final Logger log = LoggerFactory.getNotThreadSafeLogger(WebGatewayMain.class);

    public static final WebGatewayBootstrapper BOOTSTRAPPER = context ->
    {
        final var clusterChannel = context.channelToCluster();
        final var webChannel = context.channelToWebSockets();

        webChannel.registerEchoService(clusterChannel.getEchoServiceProxy());
        clusterChannel.registerEchoServiceClient(webChannel.getEchoServiceClientProxy());
    };

    public static void main(final String[] args) {
        log.info("Starting Admin Gateway").log();
        final Configurer<WebBinding> configurer = WebGateway.gateway(BOOTSTRAPPER);
        try (var webBinding = configurer.run()) {
            webBinding.awaitShutdown();
        }
    }
}
