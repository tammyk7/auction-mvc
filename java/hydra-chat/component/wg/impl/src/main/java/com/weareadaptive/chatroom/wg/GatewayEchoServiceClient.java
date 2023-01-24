package com.weareadaptive.chatroom.wg;

import com.weareadaptive.chatroom.entities.EchoResponse;
import com.weareadaptive.chatroom.entities.MutableEchoRequest;
import com.weareadaptive.chatroom.services.EchoServiceClient;
import com.weareadaptive.chatroom.services.EchoServiceProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

import java.util.HashMap;
import java.util.Map;

public class GatewayEchoServiceClient implements EchoServiceClient {

    private final Logger log = LoggerFactory.getNotThreadSafeLogger(GatewayEchoServiceClient.class);
    final EchoServiceProxy serviceProxy;
    final Map<UniqueId, String> activeRequests;

    public GatewayEchoServiceClient(EchoServiceProxy serviceProxy) {

        this.serviceProxy = serviceProxy;
        this.activeRequests = new HashMap<>();
    }

    public void onEchoResponse(UniqueId correlationId, EchoResponse echoResponse){
        String expectedResponse = activeRequests.remove(correlationId);
        if (expectedResponse == null || !expectedResponse.equals(echoResponse.body())) {
            log.error("Echo service did not match for "+correlationId).log();
        }
    }

    public void callEchoService() {

        UniqueId correlationId = serviceProxy.allocateCorrelationId();
        String textToEcho = "test";

        try (MutableEchoRequest request = serviceProxy.acquireEchoRequest()) {
            activeRequests.put(correlationId, textToEcho);
            request.body(textToEcho);
            serviceProxy.echo(correlationId, request);
        }
    }

}
