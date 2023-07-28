package com.weareadaptive.assignment_b.trading;

import com.weareadaptive.assignment_b.trading.components.TradingGateway;
import com.weareadaptive.assignment_b.trading.components.TradingGatewayBootstrapper;
import com.weareadaptive.assignment_b.trading.service.UserServiceClientImpl;
import com.weareadaptive.hydra.platform.web.bindings.WebBinding;

public class TradingGatewayMain
{
    public static final TradingGatewayBootstrapper BOOTSTRAPPER = context ->
    {
        final UserServiceClientImpl userServiceClientImpl = new UserServiceClientImpl();
        context.channelToCluster().registerUserServiceClient(userServiceClientImpl);
    };

    public static void main(final String[] args)
    {
        try (final WebBinding webBinding = TradingGateway.gateway(BOOTSTRAPPER)
            .withConfigProperty("hydra.execution.executorThreadCount", "10")
            .run()
        )
        {
            webBinding.awaitShutdown();
        }
    }
}
