package com.weareadaptive.assignment_b.admin;

import com.weareadaptive.assignment_b.admin.components.AdminGateway;
import com.weareadaptive.assignment_b.admin.components.AdminGatewayConnection;
import com.weareadaptive.assignment_b.admin.service.UserGatewayService;
import com.weareadaptive.assignment_b.engine.services.UserServiceProxy;

import io.vertx.core.Vertx;

public class AdminGatewayMain
{

    public static void main(final String[] args)
    {
        final AdminGatewayConnection conn = AdminGateway.instance().run();
        final UserServiceProxy userServiceProxy = conn.services().channelToCluster().getUserServiceProxy();

        final UserGatewayService service = new UserGatewayService(userServiceProxy);
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(service);

        conn.services().channelToCluster().registerUserServiceClient(service);
        conn.connect();
    }
}
