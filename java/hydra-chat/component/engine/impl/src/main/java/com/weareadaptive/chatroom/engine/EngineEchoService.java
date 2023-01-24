package com.weareadaptive.chatroom.engine;

import com.weareadaptive.chatroom.entities.EchoRequest;
import com.weareadaptive.chatroom.entities.MutableEchoResponse;
import com.weareadaptive.chatroom.services.EchoService;
import com.weareadaptive.chatroom.services.EchoServiceClientProxy;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;
import com.weareadaptive.hydraplatform.errornotifications.entities.MutableErrorNotification;

public class EngineEchoService implements EchoService {

    private final EchoServiceClientProxy clientProxy;

    public EngineEchoService(final EchoServiceClientProxy clientProxy) {
        this.clientProxy = clientProxy;
    }

    @Override
    public void echo(final UniqueId correlationId, final EchoRequest echoRequest) {

        try (final MutableEchoResponse response = clientProxy.acquireEchoResponse()) {
            response.body(echoRequest.body());
            clientProxy.onEchoResponse(correlationId, response);
        }

        try (final MutableErrorNotification error = clientProxy.acquireErrorNotification()) {
            error.notLoggedIn();
            clientProxy.onEchoErrorResponse(correlationId, error);
        }
    }

}
