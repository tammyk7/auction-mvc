package com.weareadaptive.echo.engine.services;

import com.weareadaptive.echo.entities.Echo;
import com.weareadaptive.echo.entities.EchoRequest;
import com.weareadaptive.echo.entities.MutableEcho;
import com.weareadaptive.echo.entities.MutableEchoResponse;
import com.weareadaptive.echo.services.EchoService;
import com.weareadaptive.echo.services.EchoServiceClientProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

public class EchoServiceImpl implements EchoService
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(EchoServiceImpl.class);
    private final EchoServiceClientProxy echoServiceClientProxy;

    public EchoServiceImpl(final EchoServiceClientProxy echoServiceClientProxy)
    {
        this.echoServiceClientProxy = echoServiceClientProxy;
    }

    @Override
    public void echoFireAndForget(final UniqueId correlationId)
    {
        LOGGER.info("Received message from echoFireAndForget").log();

        // comment out the code block below to turn off broadcast message that is triggered upon fire-and-forget message
        try (final MutableEcho echo = echoServiceClientProxy.acquireEcho())
        {
            echoServiceClientProxy.onEchoToEverybody(echo.message("This Echo broadcast message was triggered by the echoFireAndForget"));
        }
    }

    @Override
    public void echoFireAndForgetWithMessage(final UniqueId correlationId, final Echo echo)
    {
        LOGGER.info("Received following Echo message from echoFireAndForgetWithMessage: ").append(echo.message().toString()).log();
    }

    @Override
    public void echoWithReply(final UniqueId correlationId, final EchoRequest echoRequest)
    {
        LOGGER.info("Received following Echo message from echoWithReply: ").append(echoRequest.message()).log();
        try (final MutableEchoResponse echoResponse = echoServiceClientProxy.acquireEchoResponse())
        {
            echoResponse.message(echoRequest.message().toString().concat(" This is a reply from the service!"));
            echoServiceClientProxy.onEchoWithReplyResponse(correlationId, echoResponse);
        }
    }

    @Override
    public void echoRespondManyTimes(final UniqueId correlationId, final EchoRequest echoRequest)
    {
        LOGGER.info("Received following Echo message from echoRespondManyTimes: ").append(echoRequest.message()).log();
        try (final MutableEchoResponse echoResponse = echoServiceClientProxy.acquireEchoResponse())
        {
            for (int i = 0; i < 3; i++)
            {
                echoResponse.message(echoRequest.message().toString().concat(" This is a stream reply from the service!"));
                echoServiceClientProxy.onEchoRespondManyTimesResponse(correlationId, echoResponse);
            }
        }

        //comment out the line below to allow more messages to be sent over this stream
        echoServiceClientProxy.onEchoRespondManyTimesResponseCompleted(correlationId);
    }

    @Override
    public void cancelEchoRespondManyTimes(final UniqueId correlationId)
    {
        LOGGER.info("Client cancelled echoRespondManyTimes stream response").log();
    }
}
