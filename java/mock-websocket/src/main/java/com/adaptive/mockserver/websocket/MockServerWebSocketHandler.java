package com.adaptive.mockserver.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MockServerWebSocketHandler implements WebSocketHandler {

    private MockServerWebSocketPublisher mockServerWebSocketPublisher;

    private ObjectMapper objectMapper;

    public MockServerWebSocketHandler(MockServerWebSocketPublisher mockServerWebSocketPublisher) {

        this.mockServerWebSocketPublisher = mockServerWebSocketPublisher;
        objectMapper = new ObjectMapper();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        log.info("Received new request in web socket handler, session id: " + session.getId());

        Flux<String> publisher = session
                .receive()
                .doOnNext(n -> log.trace("Inbound: {}", n.getPayloadAsText()))
                .map(WebSocketMessage::getPayloadAsText)
                .map(wsMessage -> getMessageAsJsonNodes(wsMessage))
                .flatMap(jsonNodes -> {
                    try {
                        return mockServerWebSocketPublisher.processWebSocketMessage(jsonNodes);
                    } catch (Exception e) {
                        log.error("{}\n{}", e);
                        return Flux.error(e);
                    }
                })
                .doFinally(signal ->
                           {
                               log.info("Closing websocket session: " + session.getId() + " with signal: " + signal.toString());
                               session.close();
                           });

        return session.send(publisher.doOnNext(n -> log.trace("Outbound: {}", n)).map(session::textMessage));
    }

    private JsonNode getMessageAsJsonNodes(String wsMessage) {
        JsonNode messageAsJsonNodes = null;
        try {
            return objectMapper.readTree(wsMessage);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse inbound WebSocket message to Json. Message: {}, exception: {}", wsMessage, e.getMessage(), e);
        }
        return null;
    }
}
