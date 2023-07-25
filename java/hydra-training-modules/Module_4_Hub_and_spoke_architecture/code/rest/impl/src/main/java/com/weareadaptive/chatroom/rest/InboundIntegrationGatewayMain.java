package com.weareadaptive.chatroom.rest;

import com.weareadaptive.chatroom.rest.components.InboundIntegrationGateway;
import com.weareadaptive.chatroom.rest.components.InboundIntegrationGatewayConnection;
import com.weareadaptive.chatroom.rest.services.MessageService;
import com.weareadaptive.chatroom.services.ChatRoomServiceProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

import io.vertx.core.Vertx;

public class InboundIntegrationGatewayMain
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(InboundIntegrationGateway.class);

    public static void main(final String[] args)
    {
        // Start a connection and get ChatRoomServiceProxy from it
        final InboundIntegrationGatewayConnection conn = InboundIntegrationGateway.instance().run();
        final ChatRoomServiceProxy chatRoomServiceProxy = conn.services().channelToCluster().getChatRoomServiceProxy();

        final MessageService service = new MessageService(chatRoomServiceProxy);
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(service);

        conn.services().channelToCluster().registerChatRoomServiceClient(service);

        callbackToCluster(conn, service);
    }

    private static void callbackToCluster(final InboundIntegrationGatewayConnection conn, final MessageService service)
    {
        conn.services().lifecycle().onAvailable(service::start);
        conn.services().lifecycle().onUnavailable(() ->
        {
            LOGGER.info("Disconnected from the cluster").log();
        });
        conn.connect();
    }
}
