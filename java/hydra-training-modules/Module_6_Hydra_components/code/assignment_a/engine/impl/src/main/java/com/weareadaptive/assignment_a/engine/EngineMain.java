package com.weareadaptive.assignment_a.engine;

import com.weareadaptive.assignment_a.engine.service.UserServiceImpl;
import com.weareadaptive.assignment_a.engine.components.Engine;
import com.weareadaptive.assignment_a.engine.components.EngineBootstrapper;
import com.weareadaptive.assignment_a.engine.components.EngineContext;

import com.weareadaptive.assignment_a.engine.services.UserService;
import com.weareadaptive.assignment_a.engine.services.UserServiceClientProxy;
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
        registerUserService(context);
    };

    private static void registerUserService(final EngineContext context)
    {
        LOGGER.info("Registering UserService").log();
        final EngineDatabase engineDatabase = new EngineDatabase();
        context.registerDatabase(engineDatabase);
        final UserServiceClientProxy userServiceClientProxy = context.channelToClients().getUserServiceClientProxy();
        final UserService service = new UserServiceImpl(userServiceClientProxy, engineDatabase.userRecord());
        context.channelToClients().registerUserService(service);

        LOGGER.info("Registered UserService").log();
    }

    public static void main(final String[] args)
    {
        try (ClusterNodeBinding binding = Engine.node(BOOTSTRAPPER, new MillisecondClusterClock()).run())
        {
            binding.awaitShutdown();
        }
    }
}
