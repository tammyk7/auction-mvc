package com.adaptive.mockserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteResponse {
    String messageType;
    String symbol;
    String askExchangeCode;
    String askQuote;
    String askSize;
    String bidExchangeCode;
    String bidQuote;
    String bidSize;
    String timestamp;
    String[] quoteCondition;
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

    @JsonProperty("ax")
    public String getAskExchangeCode() {
        return askExchangeCode;
    }

    @JsonProperty("ax")
    public void setAskExchangeCode(String askExchangeCode) {
        this.askExchangeCode = askExchangeCode;
    }

    @JsonProperty("ap")
    public String getAskQuote() {
        return askQuote;
    }

    @JsonProperty("ap")
    public void setAskQuote(String askQuote) {
        this.askQuote = askQuote;
    }

    @JsonProperty("as")
    public String getAskSize() {
        return askSize;
    }

    @JsonProperty("as")
    public void setAskSize(String askSize) {
        this.askSize = askSize;
    }

    @JsonProperty("bx")
    public String getBidExchangeCode() {
        return bidExchangeCode;
    }

    @JsonProperty("bx")
    public void setBidExchangeCode(String bidExchangeCode) {
        this.bidExchangeCode = bidExchangeCode;
    }

    @JsonProperty("bp")
    public String getBidQuote() {
        return bidQuote;
    }

    @JsonProperty("bp")
    public void setBidQuote(String bidQuote) {
        this.bidQuote = bidQuote;
    }

    @JsonProperty("bs")
    public String getBidSize() {
        return bidSize;
    }

    @JsonProperty("bs")
    public void setBidSize(String bidSize) {
        this.bidSize = bidSize;
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
    public String[] getQuoteCondition() {
        return quoteCondition;
    }

    @JsonProperty("c")
    public void setQuoteCondition(String[] quoteCondition) {
        this.quoteCondition = quoteCondition;
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
