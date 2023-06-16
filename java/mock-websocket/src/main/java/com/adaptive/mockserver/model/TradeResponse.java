package com.adaptive.mockserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TradeResponse {
    @JsonProperty("T")
    String messageType;
    @JsonProperty("S")
    String symbol;

    @JsonProperty("i")
    int tradeId;
    @JsonProperty("x")
    String exchangeCode;
    @JsonProperty("p")
    String tradePrice;
    @JsonProperty("s")
    int tradeSize;

    @JsonProperty("c")
    String[] condition;
    @JsonProperty("t")
    String timestamp;
    @JsonProperty("z")
    String tape;

    @JsonProperty("T")
    public String getMessageType() {
        return messageType;
    }

    @JsonProperty("T")
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @JsonProperty("S")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("S")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("i")
    public int getTradeId() {
        return tradeId;
    }

    @JsonProperty("i")
    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    @JsonProperty("x")
    public String getExchangeCode() {
        return exchangeCode;
    }
    @JsonProperty("x")
    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }
    @JsonProperty("p")
    public String getTradePrice() {
        return tradePrice;
    }
    @JsonProperty("p")
    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }
    @JsonProperty("s")
    public int getTradeSize() {
        return tradeSize;
    }
    @JsonProperty("s")
    public void setTradeSize(int tradeSize) {
        this.tradeSize = tradeSize;
    }

    @JsonProperty("t")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("t")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("c")
    public String[] getCondition() {
        return condition;
    }

    @JsonProperty("c")
    public void setCondition(String[] quoteCondition) {
        this.condition = quoteCondition;
    }

    @JsonProperty("z")
    public String getTape() {
        return tape;
    }

    @JsonProperty("z")
    public void setTape(String tape) {
        this.tape = tape;
    }
}
