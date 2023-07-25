package com.weareadaptive.echo.wg.service_client;

import com.weareadaptive.echo.entities.Echo;
import com.weareadaptive.echo.entities.EchoResponse;
import com.weareadaptive.echo.services.EchoServiceClient;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

public class EchoServiceClientImpl implements EchoServiceClient
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(EchoServiceClientImpl.class);

    @Override
    public void onEchoWithReplyResponse(final UniqueId correlationId, final EchoResponse echoResponse)
    {
        LOGGER.info("Received EchoResponse from echoWithReply: ".concat(echoResponse.message().toString())).log();
    }

    @Override
    public void onEchoRespondManyTimesResponse(final UniqueId correlationId, final EchoResponse echoResponse)
    {
        LOGGER.info("Received EchoResponse from echoRespondManyTimes: ".concat(echoResponse.message().toString())).log();
    }

    @Override
    public void onEchoRespondManyTimesResponseCompleted(final UniqueId correlationId)
    {
        LOGGER.info("Received echoRespondManyTimes completion").log();
    }

    @Override
    public void onEchoToEverybody(final Echo echo)
    {
        LOGGER.info("Received Echo from echoToEverybody: ".concat(echo.message().toString())).log();
    }
}
