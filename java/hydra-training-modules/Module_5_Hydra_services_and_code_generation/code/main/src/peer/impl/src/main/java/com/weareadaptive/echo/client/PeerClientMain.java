package com.weareadaptive.echo.client;

import com.weareadaptive.echo.client.components.PeerClient;
import com.weareadaptive.echo.client.components.PeerClientConnection;
import com.weareadaptive.echo.wg.components.EchoGatewayChannel;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import com.weareadaptive.hydra.shared.ShutdownUtil;

import org.agrona.CloseHelper;

public class PeerClientMain
{

    public static void main(final String[] args)
    {
        final PeerClientConnection peerClientConnection = PeerClient.instance().run();
        final EchoGatewayChannel echoGatewayChannel = peerClientConnection.services().toPeerEchoGatewayChannel();
        echoGatewayChannel.connect();

        echoGatewayChannel.lifecycle().onAvailable(() ->
        {
            final var echoServiceProxy = echoGatewayChannel.getEchoServiceProxy();
            try (var echo = echoServiceProxy.acquireEchoRequest())
            {
                final UniqueId correlationId = echoServiceProxy.allocateCorrelationId();
                echo.message("This message originated from PeerClient!");
                echoServiceProxy.echoWithReply(correlationId, echo);
            }
        });

        ShutdownUtil.shutdownSignalBarrier().await();
        CloseHelper.close(peerClientConnection);
    }
}
