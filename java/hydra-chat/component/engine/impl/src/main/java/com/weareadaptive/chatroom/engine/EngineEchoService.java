package com.weareadaptive.chatroom.engine;

import com.weareadaptive.chatroom.entities.EchoRequest;
import com.weareadaptive.chatroom.entities.HelloRequest;
import com.weareadaptive.chatroom.entities.MutableEchoResponse;
import com.weareadaptive.chatroom.entities.MutableHelloResponse;
import com.weareadaptive.chatroom.services.EchoService;
import com.weareadaptive.chatroom.services.EchoServiceClientProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

public class EngineEchoService implements EchoService {

    private final Logger log = LoggerFactory.getNotThreadSafeLogger(EngineEchoService.class);

    private final EchoServiceClientProxy clientProxy;

    public EngineEchoService(final EchoServiceClientProxy clientProxy) {
        this.clientProxy = clientProxy;
    }

    @Override
    public void echo(final UniqueId correlationId, final EchoRequest echoRequest) {
        log.info("Handling Echo Request: ").append(echoRequest.body()).log();

        try (final MutableEchoResponse response = clientProxy.acquireEchoResponse()) {
            response.body(echoRequest.body());
            clientProxy.onEchoResponse(correlationId, response);
        }
    }

    @Override
    public void hello(final UniqueId correlationId, final HelloRequest helloRequest) {
        log.info("Handling Hello Request: ").append(helloRequest.body()).log();

        try (final MutableHelloResponse response = clientProxy.acquireHelloResponse()) {
            response.body("Hello " + helloRequest.body().toString());
            clientProxy.onHelloResponse(correlationId, response);
        }
    }
}
