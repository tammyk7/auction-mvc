package com.weareadaptive.assignment_b.admin.service;

import static io.vertx.core.http.HttpMethod.POST;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.weareadaptive.assignment_b.engine.entities.MutableCreateUserRequest;
import com.weareadaptive.assignment_b.engine.services.UserServiceClient;
import com.weareadaptive.assignment_b.engine.services.UserServiceProxy;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class UserGatewayService extends AbstractVerticle implements UserServiceClient
{
    private final UserServiceProxy userServiceProxy;
    private final Map<UniqueId, RoutingContext> requestsById;

    public UserGatewayService(final UserServiceProxy userServiceProxy)
    {
        this.userServiceProxy = userServiceProxy;
        requestsById = new ConcurrentHashMap<>();
    }

    @Override
    public void start()
    {
        final int port = 8080;
        final Router router = Router.router(vertx);
        configureRoutes(router);

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(port);

    }

    public void configureRoutes(final Router router)
    {
        router.route().handler(BodyHandler.create().setBodyLimit(2048));
        router.route("/createUser").method(POST).handler(this::createUser);
    }

    private void createUser(final RoutingContext routingContext)
    {
        final JsonObject jsonObject = routingContext.body().asJsonObject();

        // Saving routing context with correlation id
        final UniqueId correlationId = userServiceProxy.allocateCorrelationId();
        requestsById.put(correlationId, routingContext);

        try (final MutableCreateUserRequest request = userServiceProxy.acquireCreateUserRequest())
        {
            request.username(jsonObject.getString("username"));
            request.age(jsonObject.getInteger("age").byteValue());
            userServiceProxy.createUser(correlationId, request);
        }
    }
}
