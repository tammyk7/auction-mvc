package com.weareadaptive.oms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.weareadaptive.oms.util.ExecutionResult;
import com.weareadaptive.oms.util.Side;
import com.weareadaptive.oms.util.Status;

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
        var result = orderbook.placeOrder(10.00, 5, Side.BID);
        assertEquals(Status.RESTING, result.getStatus());
    }

    @Test
    @DisplayName("Non-crossing ASK is placed, and returns its orderId and a RESTING status")
    public void placeRestingAsk()
    {
        ExecutionResult result = orderbook.placeOrder(10.00, 5, Side.BID);
        assertEquals(Status.RESTING, result.getStatus());
    }

    @Test
    @DisplayName("Crossing BID that will be partially filled is placed and returns its orderId and a PARTIAL status")
    public void placePartialBid()
    {
        orderbook.placeOrder(10.00, 20, Side.ASK);
        ExecutionResult result = orderbook.placeOrder(10.00, 40, Side.BID);
        assertEquals(Status.PARTIAL, result.getStatus());
    }

    @Test
    @DisplayName("Crossing ASK that will be partially filled is placed and returns its orderId and a PARTIAL status")
    public void placePartialAsk()
    {
        orderbook.placeOrder(10.00, 20, Side.BID);
        ExecutionResult result = orderbook.placeOrder(10.00, 40, Side.ASK);
        assertEquals(Status.PARTIAL, result.getStatus());
    }

    @Test
    @DisplayName("Crossing BID that will be filled entirely is placed and returns its orderId and a FILLED status")
    public void placeFilledBid()
    {
        orderbook.placeOrder(9.00, 20, Side.ASK);
        orderbook.placeOrder(10.00, 20, Side.ASK);
        orderbook.placeOrder(10.50, 20, Side.ASK);
        ExecutionResult result = orderbook.placeOrder(10.00, 40, Side.BID);
        assertEquals(Status.FILLED, result.getStatus());
    }

    @Test
    @DisplayName("Crossing ASK that will be filled entirely is placed and returns its orderId and a FILLED status")
    public void placeFilledAsk()
    {
        orderbook.placeOrder(10.00, 20, Side.BID);
        orderbook.placeOrder(11.00, 20, Side.BID);
        orderbook.placeOrder(12.00, 20, Side.BID);
        ExecutionResult result = orderbook.placeOrder(10.00, 40, Side.ASK);
        assertEquals(Status.FILLED, result.getStatus());
    }

    @Test
    @DisplayName("BID is cancelled and returns its orderId and a CANCELLED status")
    public void cancelBid()
    {
        ExecutionResult placeResult = orderbook.placeOrder(10.00, 20, Side.BID);
        ExecutionResult result = orderbook.cancelOrder(placeResult.getOrderId());
        assertEquals(Status.CANCELLED, result.getStatus());
    }

    @Test
    @DisplayName("ASK is cancelled and returns its orderId and a CANCELLED status")
    public void cancelAsk()
    {
        ExecutionResult placeResult = orderbook.placeOrder(10.00, 20, Side.ASK);
        ExecutionResult result = orderbook.cancelOrder(placeResult.getOrderId());
        assertEquals(Status.CANCELLED, result.getStatus());
    }

    @Test
    @DisplayName("Non-existing orderId is used to cancel a Order and returns the attempted orderId and a NONE status")
    public void cancelNonExistingBid()
    {
        orderbook.placeOrder(10.00, 20, Side.ASK);
        orderbook.placeOrder(9.00, 20, Side.BID);
        orderbook.placeOrder(11.00, 20, Side.ASK);
        ExecutionResult result = orderbook.cancelOrder(12);
        assertEquals(Status.NONE, result.getStatus());
    }

    @Test
    @DisplayName("All orderbook orders are cleared and should be empty when checked, orderId should not be reset.")
    public void clearOrderbook()
    {
        orderbook.placeOrder(10.00, 20, Side.ASK);
        orderbook.placeOrder(9.00, 20, Side.BID);
        orderbook.placeOrder(11.00, 20, Side.ASK);
        orderbook.clear();
        assertTrue(orderbook.isClear());
    }

    @Test
    @DisplayName("Entire orderbook state is reset, all states should be at initial values or empty.")
    public void resetOrderbook()
    {
        orderbook.placeOrder(10.00, 20, Side.ASK);
        orderbook.placeOrder(9.00, 20, Side.BID);
        orderbook.placeOrder(11.00, 20, Side.ASK);
        orderbook.reset();
        assertTrue(orderbook.isReset());
    }

}
