package com.weareadaptive.echo.engine;

import com.weareadaptive.echo.engine.services.EchoServiceImpl;
import com.weareadaptive.echo.engine.components.Engine;
import com.weareadaptive.echo.engine.components.EngineBootstrapper;
import com.weareadaptive.echo.engine.components.EngineContext;
import com.weareadaptive.echo.services.EchoService;
import com.weareadaptive.echo.services.EchoServiceClientProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.engine.bindings.ClusterNodeBinding;
import io.aeron.cluster.MillisecondClusterClock;

public class EngineMain
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(EngineMain.class);
    public static final EngineBootstrapper BOOTSTRAPPER = context ->
    {
        LOGGER.info("Bootstrapping Engine").log();
        registerEchoService(context);
    };

    private static void registerEchoService(final EngineContext context)
    {
        LOGGER.info("Registering EchoService").log();
        final EchoServiceClientProxy echoServiceClientProxy = context.channelToClients().getEchoServiceClientProxy();
        final EchoService service = new EchoServiceImpl(echoServiceClientProxy);
        context.channelToClients().registerEchoService(service);

        LOGGER.info("Registered EchoService").log();
    }

    public static void main(final String[] args)
    {
        try (ClusterNodeBinding binding = Engine.node(BOOTSTRAPPER, new MillisecondClusterClock()).run())
        {
            binding.awaitShutdown();
        }
    }

}
