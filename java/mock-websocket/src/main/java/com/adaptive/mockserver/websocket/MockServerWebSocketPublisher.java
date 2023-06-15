package com.adaptive.mockserver.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.CorePublisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;


@Slf4j
@Service
public class MockServerWebSocketPublisher {

    private ObjectMapper objectMapper;

    static final String MSG_ACTION = "action";
    static final String AUTH = "auth";
    static final String SUBSCRIBE = "subscribe";
    static final String UNSUBSCRIBE = "unsubscribe";

    ConcurrentLinkedDeque<Disposable> openStreams;

    public MockServerWebSocketPublisher() {
        objectMapper = new ObjectMapper();
        openStreams = new ConcurrentLinkedDeque<>();
    }

    public CorePublisher<String> processWebSocketMessage(JsonNode messageAsJsonNodes) {

        try {
            if (messageAsJsonNodes.has(MSG_ACTION) && messageAsJsonNodes.get(MSG_ACTION).textValue().equalsIgnoreCase(AUTH)) {
                return handleAuthMessage(messageAsJsonNodes);
            }
            if (messageAsJsonNodes.has(MSG_ACTION) && messageAsJsonNodes.get(MSG_ACTION).textValue().equalsIgnoreCase(SUBSCRIBE)) {
                return handleSubscribe(messageAsJsonNodes);
            }
            if (messageAsJsonNodes.has(MSG_ACTION) && messageAsJsonNodes.get(MSG_ACTION).textValue().equalsIgnoreCase(UNSUBSCRIBE)) {
                return handleUnsubscribe();
            }
        } catch (Exception e) {
            log.error("Error while processing inbound live market data message, exception: {}", e);
        }
        return Flux.empty();
    }

    private CorePublisher<String> handleUnsubscribe() {
        openStreams.forEach(s -> {
            if (!s.isDisposed()) {
                s.dispose();
            }
        });
        return Flux.just("{\"action\":\"subscribe\",\"trades\":[],\"quotes\":[],\"bars\":[]}");
    }

    private Flux<String> handleAuthMessage(JsonNode messageAsJsonNodes) {
        try {
            MarketDataAuthorizeRequest authMsg = objectMapper.treeToValue(messageAsJsonNodes, MarketDataAuthorizeRequest.class);
            if (Objects.equals(authMsg.getKey(), "") || Objects.equals(authMsg.getSecret(), "")) {
                return createAuthFailedResponse();
            }
        } catch (JsonProcessingException e) {
            log.error("Unable to parse market data jsonNode to quotes:  {}", e.getMessage(), e);
            return createAuthFailedResponse();
        }
        return Flux.just("[{\"T\":\"success\",\"msg\":\"authenticated\"}]");
    }

    private Flux<String> handleSubscribe(JsonNode jsonNode) {
        try {
            MarketDataRequest marketDataRequest = objectMapper.treeToValue(jsonNode, MarketDataRequest.class);

            if (marketDataRequest.getQuotes() != null && marketDataRequest.getQuotes().length > 0) {

                Flux<String> quotesFlux = Flux
                        .create(sink -> createQuotesSinkToEmitData(marketDataRequest.getQuotes(), sink));

                return quotesFlux.doOnSubscribe(s -> openStreams.add(() -> s.cancel()));

            } else if (marketDataRequest.getTrades() != null && marketDataRequest.getTrades().length > 0) {
                Flux<String> tradesFlux = Flux
                        .create(sink -> createTradesSinkToEmitData(marketDataRequest.getTrades(), sink));

                return tradesFlux.doOnSubscribe(s -> openStreams.add(() -> s.cancel()));

            } else {
                return createInvalidSyntaxResponse();
            }
        } catch (JsonProcessingException e) {
            log.error("Unable to parse incoming request: ", e);
            return createInvalidSyntaxResponse();
        }
    }

    private void createQuotesSinkToEmitData(String[] symbols, FluxSink<String> sink) {

        Random random = new Random();
        long interval = random.nextLong(75, 150);

        Flux.interval(Duration.ofMillis(interval)).doOnNext(n -> {
            int symbolIndex = 0;
            if (symbols.length > 1) {
                symbolIndex = random.nextInt(symbols.length);
            }
            String symbol = symbols[symbolIndex];

            QuoteResponse quoteResponse = getQuoteResponse(symbol, random);
            QuoteResponse quoteResponse2 = getQuoteResponse(symbol, random);
            QuoteResponse quoteResponse3 = getQuoteResponse(symbol, random);
            QuoteResponse quoteResponse4 = getQuoteResponse(symbol, random);

            ArrayList<QuoteResponse> quoteResponses = new ArrayList<>();

            quoteResponses.add(quoteResponse);
            quoteResponses.add(quoteResponse2);

            if (random.nextBoolean()) {
                quoteResponses.add(quoteResponse3);
            }
            if (random.nextBoolean()) {
                quoteResponses.add(quoteResponse4);
            }

            String quotes = mapObjectToJson(quoteResponses);

            sink.next(quotes);
        }).subscribe();
    }

    private void createTradesSinkToEmitData(String[] symbols, FluxSink<String> sink) {

        Random random = new Random();
        long interval = random.nextLong(75, 150);

        Flux.interval(Duration.ofMillis(interval)).doOnNext(n -> {
            int symbolIndex = 0;
            if (symbols.length > 1) {
                symbolIndex = random.nextInt(symbols.length);
            }
            String symbol = symbols[symbolIndex];

            TradeResponse tradeResponse = getTradeResponse(symbol, random);
            TradeResponse tradeResponse2 = getTradeResponse(symbol, random);
            TradeResponse tradeResponse3 = getTradeResponse(symbol, random);

            ArrayList<TradeResponse> tradeResponses = new ArrayList<>();
            tradeResponses.add(tradeResponse);
            if (random.nextBoolean()) {
                tradeResponses.add(tradeResponse2);
            }
            if (random.nextBoolean()) {
                tradeResponses.add(tradeResponse3);
            }

            String trades = mapObjectToJson(tradeResponses);

            sink.next(trades);
        }).subscribe();
    }

    private QuoteResponse getQuoteResponse(String symbol, Random random) {
        QuoteResponse quoteResponse = new QuoteResponse();
        quoteResponse.setSymbol(symbol);

        int priceInt = random.nextInt(3, 3000);
        int priceFloat = random.nextInt(10, 9990);

        quoteResponse.setAskQuote(String.format("%s.%s", priceInt, priceFloat - 2));
        quoteResponse.setBidQuote(String.format("%s.%s", priceInt, priceFloat + 2));
        quoteResponse.setQuoteCondition(new String[]{"mock"});
        quoteResponse.setTape("A");
        Integer askSize = random.nextInt(1, 1000);
        quoteResponse.setAskSize(askSize.toString());
        Integer bidSize = random.nextInt(1, 1000);
        quoteResponse.setBidSize(bidSize.toString());
        quoteResponse.setTimestamp(Instant.now().toString());
        quoteResponse.setAskExchangeCode("mock");
        quoteResponse.setBidExchangeCode("mock");
        // q means quotes
        quoteResponse.setMessageType("q");
        return quoteResponse;
    }

    private TradeResponse getTradeResponse(String symbol, Random random) {
        TradeResponse tradeResponse = new TradeResponse();
        tradeResponse.setSymbol(symbol);
        tradeResponse.setTape("A");
        tradeResponse.setTradeId(random.nextInt());
        int priceInt = random.nextInt(3, 3000);
        int priceFloat = random.nextInt(999);
        tradeResponse.setTradePrice(String.format("%s.%s", priceInt, priceFloat));
        tradeResponse.setExchangeCode("mock");
        tradeResponse.setCondition(new String[]{"mock"});
        tradeResponse.setTimestamp(Instant.now().toString());
        tradeResponse.setMessageType("t");
        return tradeResponse;
    }

    private Flux<String> createInvalidSyntaxResponse() {
        return Flux.just("[{\"T\":\"error\",\"code\":400,\"msg\":\"invalid syntax\"}]");
    }

    private Flux<String> createAuthFailedResponse() {
        return Flux.just("[{\"T\":\"error\",\"code\":402,\"msg\":\"auth failed\"}]");
    }
}
