package com.weareadaptive.chatroom.rest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

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
    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    public static void main(final String[] args) throws InterruptedException, IOException
    {
        // Start a connection and get ChatRoomServiceProxy from it
        final InboundIntegrationGatewayConnection conn = InboundIntegrationGateway.instance().run();
        final ChatRoomServiceProxy chatRoomServiceProxy = conn.services().channelToCluster().getChatRoomServiceProxy();

        final MessageService service = new MessageService(chatRoomServiceProxy);
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(service);

        conn.services().channelToCluster().registerChatRoomServiceClient(service);

        callbackToCluster(conn);
        COUNT_DOWN_LATCH.await();
        service.start();
    }

    private static void callbackToCluster(final InboundIntegrationGatewayConnection conn)
    {
        conn.services().lifecycle().onAvailable(COUNT_DOWN_LATCH::countDown);
        conn.services().lifecycle().onUnavailable(() ->
        {
            LOGGER.info("Disconnected from the cluster").log();
        });
        conn.connect();
    }
}
