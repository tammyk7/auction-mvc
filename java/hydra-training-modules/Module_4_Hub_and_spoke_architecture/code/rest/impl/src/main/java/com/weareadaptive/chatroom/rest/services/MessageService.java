package com.weareadaptive.chatroom.rest.services;

import static io.vertx.core.http.HttpMethod.POST;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.weareadaptive.chatroom.entities.Message;
import com.weareadaptive.chatroom.entities.MessageResponse;
import com.weareadaptive.chatroom.entities.MutableMessageRequest;
import com.weareadaptive.chatroom.services.ChatRoomServiceClient;
import com.weareadaptive.chatroom.services.ChatRoomServiceProxy;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MessageService extends AbstractVerticle implements ChatRoomServiceClient
{

    private final ChatRoomServiceProxy chatRoomServiceProxy;
    private final Map<UniqueId, RoutingContext> requestsById;

    public MessageService(final ChatRoomServiceProxy chatRoomServiceProxy)
    {
        this.requestsById = new ConcurrentHashMap<>();
        this.chatRoomServiceProxy = chatRoomServiceProxy;
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
        router.route("/sendMessage").method(POST).handler(this::sendMessage);
    }

    private void sendMessage(final RoutingContext routingContext)
    {
        final JsonObject jsonObject = routingContext.body().asJsonObject();

        // Saving routing context with correlation id
        final UniqueId correlationId = chatRoomServiceProxy.allocateCorrelationId();
        requestsById.put(correlationId, routingContext);

        try (final MutableMessageRequest request = chatRoomServiceProxy.acquireMessageRequest())
        {
            request.message(jsonObject.getString("message"));
            chatRoomServiceProxy.handleMessage(correlationId, request);
        }
    }


    @Override
    public void onHandleMessageResponse(final UniqueId correlationId, final MessageResponse messageResponse)
    {
        final RoutingContext routingContext = requestsById.get(correlationId);
        if (messageResponse.isSuccess())
        {
            routingContext.response().end("Message sent to cluster");
        }
        else
        {
            routingContext.response().end("Failed to send message");
        }
    }

    @Override
    public void onChatRoomMessages(final Message message)
    {
        // No-op
    }
}
