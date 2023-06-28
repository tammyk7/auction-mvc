package com.weareadaptive.oms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderbookTest
{
    private OrderbookImpl orderbook;

    @BeforeEach
    void setUp()
    {
        this.orderbook = new OrderbookImpl();
    }

    @Test
    @DisplayName("Non-crossing BID is placed, and returns its orderId and a RESTING status")
    public void placeRestingBid()
    {
    }

    @Test
    @DisplayName("Non-crossing ASK is placed, and returns its orderId and a RESTING status")
    public void placeRestingAsk()
    {
    }

    @Test
    @DisplayName("Crossing BID that will be partially filled is placed and returns its orderId and a PARTIAL status")
    public void placePartialBid()
    {
    }

    @Test
    @DisplayName("Crossing ASK that will be partially filled is placed and returns its orderId and a PARTIAL status")
    public void placePartialAsk()
    {
    }

    @Test
    @DisplayName("Crossing BID that will be filled entirely is placed and returns its orderId and a FILLED status")
    public void placeFilledBid()
    {
    }

    @Test
    @DisplayName("Crossing ASK that will be filled entirely is placed and returns its orderId and a FILLED status")
    public void placeFilledAsk()
    {
    }

    @Test
    @DisplayName("BID is cancelled and returns its orderId and a CANCELLED status")
    public void cancelBid()
    {
    }

    @Test
    @DisplayName("ASK is cancelled and returns its orderId and a CANCELLED status")
    public void cancelAsk()
    {
    }

    @Test
    @DisplayName("Non-existing orderId is used to cancel a BID and returns the orderId and a NONE status")
    public void cancelNonExistingBid()
    {
    }

    @Test
    @DisplayName("Non-existing orderId is used to cancel a ASK and returns the orderId and a NONE status")
    public void cancelNonExistingAsk()
    {
    }

    @Test
    @DisplayName("All orderbook orders are cleared and should be empty when checked, orderId should not be reset.")
    public void clearOrderbook()
    {
    }

    @Test
    @DisplayName("Entire orderbook state is reset, all states should be at initial values or empty.")
    public void resetOrderbook()
    {
    }

}
