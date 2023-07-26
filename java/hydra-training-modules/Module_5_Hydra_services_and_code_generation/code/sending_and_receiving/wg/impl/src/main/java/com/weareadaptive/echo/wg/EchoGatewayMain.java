package com.weareadaptive.echo.wg;

import com.weareadaptive.echo.wg.service_client.EchoServiceClientImpl;
import com.weareadaptive.echo.wg.components.EchoGateway;
import com.weareadaptive.echo.wg.components.EchoGatewayBootstrapper;
import com.weareadaptive.echo.services.EchoServiceClient;
import com.weareadaptive.echo.services.EchoServiceProxy;
import com.weareadaptive.echo.wg.services.EchoServiceImpl;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import com.weareadaptive.hydra.platform.web.bindings.WebBinding;

public class EchoGatewayMain
{
    public static final EchoGatewayBootstrapper BOOTSTRAPPER = context ->
    {
        final EchoServiceProxy echoServiceProxy = context.channelToCluster().getEchoServiceProxy();
        final EchoServiceClient echoServiceClient = new EchoServiceClientImpl();

        context.channelToCluster().registerEchoServiceClient(echoServiceClient);

        // fire-and-forget without an object parameter
        final UniqueId correlationId = echoServiceProxy.allocateCorrelationId();
        echoServiceProxy.echoFireAndForget(correlationId);

        // fire-and-forget with an object parameter
        try (var echo = echoServiceProxy.acquireEcho())
        {
            final UniqueId correlationId1 = echoServiceProxy.allocateCorrelationId();
            echo.message("Hello from fire-and-forget!");
            echoServiceProxy.echoFireAndForgetWithMessage(correlationId1, echo);
        }

        // sending request-response request
        try (var echo = echoServiceProxy.acquireEchoRequest())
        {
            final UniqueId correlationId2 = echoServiceProxy.allocateCorrelationId();
            echo.message("Hello from request-response!");
            echoServiceProxy.echoWithReply(correlationId2, echo);
        }

        // sending request to provoke a stream of responses from the service
        try (var echoRequest = echoServiceProxy.acquireEchoRequest())
        {
            final UniqueId correlationId3 = echoServiceProxy.allocateCorrelationId();
            echoRequest.message("Hello from requested streams!");
            echoServiceProxy.echoRespondManyTimes(correlationId3, echoRequest);
        }

        // access peer channel
        final var peerChannel = context.toPeerClientsChannel();
        peerChannel.registerEchoService(new EchoServiceImpl(peerChannel.getEchoServiceClientProxy()));
        peerChannel.listen();
    };

    public static void main(final String[] args)
    {
        final Logger log = LoggerFactory.getNotThreadSafeLogger(EchoGatewayMain.class);
        log.info("Starting EchoGateway").log();
        try (final WebBinding webBinding = EchoGateway.gateway(BOOTSTRAPPER)
                .withConfigProperty("hydra.execution.executorThreadCount", "10")
                .run()
        )
        {
            webBinding.awaitShutdown();
        }
    }
}
