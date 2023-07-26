package com.weareadaptive.echo.client;

import com.weareadaptive.echo.client.components.PeerClient;
import com.weareadaptive.echo.client.components.PeerClientConnection;
import com.weareadaptive.echo.entities.EchoResponse;
import com.weareadaptive.echo.services.EchoServiceClient;
import com.weareadaptive.echo.wg.components.EchoGatewayChannel;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import com.weareadaptive.hydra.shared.ShutdownUtil;

import org.agrona.CloseHelper;

public class PeerClientMain
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(PeerClientMain.class);
    public static void main(final String[] args)
    {
        final PeerClientConnection peerClientConnection = PeerClient.instance().run();
        final EchoGatewayChannel echoGatewayChannel = peerClientConnection.services().toPeerEchoGatewayChannel();
        echoGatewayChannel.registerEchoServiceClient(new EchoServiceClient()
        {
            @Override
            public void onEchoWithReplyResponse(UniqueId correlationId, EchoResponse echoResponse)
            {
                LOGGER.info("Received response from EchoService: ").append(echoResponse.message()).log();
            }
        });

        echoGatewayChannel.lifecycle().onAvailable(() ->
        {
            final var echoServiceProxy = echoGatewayChannel.getEchoServiceProxy();
            // note that the method used by the proxy may be changed,
            // but additional methods may need to be implemented in the EchoServiceClient above to handle the response
            try (var echo = echoServiceProxy.acquireEchoRequest())
            {
                final UniqueId correlationId = echoServiceProxy.allocateCorrelationId();
                echo.message("This message originated from PeerClient!");
                echoServiceProxy.echoWithReply(correlationId, echo);
            }
        });

        echoGatewayChannel.connect();

        ShutdownUtil.shutdownSignalBarrier().await();
        CloseHelper.close(peerClientConnection);
    }
}
