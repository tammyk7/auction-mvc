package com.weareadaptive.assignment_b.engine;

import com.weareadaptive.assignment_b.engine.service.OrderServiceImpl;
import com.weareadaptive.assignment_b.engine.service.UserServiceImpl;
import com.weareadaptive.assignment_b.engine.components.Engine;
import com.weareadaptive.assignment_b.engine.components.EngineBootstrapper;
import com.weareadaptive.assignment_b.engine.components.EngineContext;

import com.weareadaptive.assignment_b.engine.services.UserService;
import com.weareadaptive.assignment_b.engine.services.UserServiceClientProxy;
import com.weareadaptive.assignment_b.trading.services.OrderService;
import com.weareadaptive.assignment_b.trading.services.OrderServiceClientProxy;
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
        registerOrderService(context);
    };

    private static void registerOrderService(final EngineContext context)
    {
        LOGGER.info("Registering OrderService").log();
        final OrderServiceClientProxy userServiceClientProxy = context.channelToClients().getOrderServiceClientProxy();
        final OrderService service = new OrderServiceImpl(userServiceClientProxy);
        context.channelToClients().registerOrderService(service);

        LOGGER.info("Registered OrderService").log();
    }

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
