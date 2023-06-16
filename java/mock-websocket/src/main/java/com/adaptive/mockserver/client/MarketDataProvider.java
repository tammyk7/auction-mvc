package com.adaptive.mockserver.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.adaptive.websocket.client.Json.mapObjectToJson;

public class MarketDataProvider extends WebSocketClient {
    private static final Log LOGGER = LogFactory.getLog(MarketDataProvider.class);
    private final ConcurrentHashMap<String, AtomicInteger> subscriptions;
    private boolean authenticated;
    private ObjectMapper objectMapper;
    //TODO: Thread safe
    private List<Consumer<MarketData>> onMarketData = new LinkedList<>();

    public MarketDataProvider() {
        super(URI.create("wss://add.your.server.here/"));
        objectMapper = new ObjectMapper();
        subscriptions = new ConcurrentHashMap<>();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

        var key = "";
        var secret = "";
        MarketDataAuthorizeDto marketDataAuthorizeDto = new MarketDataAuthorizeDto(
                "auth",
                key,
                secret);
        String authorizeMessage = mapObjectToJson(marketDataAuthorizeDto);
        send(authorizeMessage);
        LOGGER.info("OUTBOUND: " + authorizeMessage);
    }

    @SneakyThrows
    @Override
    public void onMessage(String s) {
        var json = objectMapper.readTree(s);
        LOGGER.info("INBOUND: " + s);

        if (json.isArray()) {

            for (final JsonNode objNode : json) {
                if (objNode.has("msg") && objNode.get("msg").asText().equals("authenticated")) {
                    authenticated = true;
                } else {
                    //System.out.println(objNode);
                    if (objNode.has("T") && objNode.get("T").asText().equals("q")) {
                        var market = new MarketData(objNode.get("S").asText());
                        onMarketData.forEach(c -> c.accept(market));
                    }
                }
            }
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    public Flux<MarketData> subscribe(String market) {
        if (!authenticated) {
            throw new RuntimeException("");
        }
        var subscribeTo = new MarketDataDto("subscribe", new String[]{}, new String[]{market}, new String[]{});
        //subscriptions.putIfAbsent()

        var count = subscriptions.computeIfAbsent(market, k -> new AtomicInteger());
        count.incrementAndGet();
        //TODO: Check if it s ok to send each time
        String subscribePayload = mapObjectToJson(subscribeTo);
        send(subscribePayload);

        return Flux.create(sink -> {
                               Consumer<MarketData> onData = m -> {
                                   if (m.symbol().equals(market)) {
                                       sink.next(m);
                                   }
                               };

                               onMarketData.add(onData);
                               sink.onDispose(() -> {
                                   onMarketData.remove(onData);
                                   var value = count.decrementAndGet();
                                   if (value == 0) {
                                       //Call unsubscribe
                                   }
                               });
                           }
        );
    }

    public record MarketData(String symbol) {
    }
}