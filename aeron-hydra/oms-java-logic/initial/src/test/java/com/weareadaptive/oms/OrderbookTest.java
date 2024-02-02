package com.weareadaptive.oms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    ExecutionResult restingBid = orderbook.placeOrder(10.24, 10, Side.BID);

    assertNotNull(restingBid);
    assertEquals(Status.RESTING, restingBid.getStatus());
    assertEquals(1, restingBid.getOrderId());
  }

  @Test
  @DisplayName("Non-crossing BID is placed with existing asks, and returns its orderId and a RESTING status")
  public void placeRestingBid2()
  {
    orderbook.placeOrder(10.50, 10, Side.ASK);
    orderbook.placeOrder(10.49, 10, Side.BID);
    ExecutionResult restingBid = orderbook.placeOrder(10.24, 10, Side.BID);

    assertNotNull(restingBid);
    assertEquals(Status.RESTING, restingBid.getStatus());
    assertEquals(3, restingBid.getOrderId());
  }

  //cover test scenarios per existing scenario

  //what if there is an existing bid in the book

  @Test
  @DisplayName("Place an ask with an existing ask in the book")
  public void placeAskWithAnExistingAsk()
  {
    orderbook.placeOrder(10.00, 5, Side.ASK);
    orderbook.placeOrder(8.00, 6, Side.ASK);

    //place ask
    //place a bid
    //check to see if its updating
    //place a bid

    //order book has been updated

    //place a bid -> should have 0 - nothing to bid on

  }

  //what if there is an existing ask in the book

  @Test
  @DisplayName("Non-crossing ASK is placed, and returns its orderId and a RESTING status")
  public void placeRestingAsk()
  {
    ExecutionResult restingBid = orderbook.placeOrder(10.24, 4, Side.ASK);

    assertNotNull(restingBid);
    assertEquals(Status.RESTING, restingBid.getStatus());
    assertEquals(1, restingBid.getOrderId());
  }

  @Test
  @DisplayName("Crossing BID that will be partially filled is placed and returns its orderId and a PARTIAL status")
  public void placePartialBid()
  {
    orderbook.placeOrder(9.99, 1, Side.ASK);
    orderbook.placeOrder(10.00, 3, Side.ASK);

    ExecutionResult partialBid = orderbook.placeOrder(10.00, 5, Side.BID);

    assertNotNull(partialBid);
    assertEquals(Status.PARTIAL, partialBid.getStatus());
    assertEquals(3, partialBid.getOrderId());

    //
  }

  @Test
  @DisplayName("Crossing ASK that will be partially filled is placed and returns its orderId and a PARTIAL status")
  public void placePartialAsk()
  {
    orderbook.placeOrder(10.00, 3, Side.BID);

    ExecutionResult partialBid = orderbook.placeOrder(10.00, 5, Side.ASK);

    assertNotNull(partialBid);
    assertEquals(Status.PARTIAL, partialBid.getStatus());
    assertEquals(2, partialBid.getOrderId());
  }

  @Test
  @DisplayName("Crossing BID that will be filled entirely is placed and returns its orderId and a FILLED status")
  public void placeFilledBid()
  {
    orderbook.placeOrder(10.00, 5, Side.ASK);

    ExecutionResult filledBid = orderbook.placeOrder(10.00, 5, Side.BID);

    assertNotNull(filledBid);
    assertEquals(Status.FILLED, filledBid.getStatus());
    assertEquals(2, filledBid.getOrderId());
  }

  @Test
  @DisplayName("Crossing ASK that will be filled entirely is placed and returns its orderId and a FILLED status")
  public void placeFilledAsk()
  {
    orderbook.placeOrder(10.00, 5, Side.BID);

    ExecutionResult filledBid = orderbook.placeOrder(10.00, 5, Side.ASK);

    assertNotNull(filledBid);
    assertEquals(Status.FILLED, filledBid.getStatus());
    assertEquals(2, filledBid.getOrderId());
  }

  @Test
  @DisplayName("BID is cancelled and returns its orderId and a CANCELLED status")
  public void cancelBid()
  {
    ExecutionResult order = orderbook.placeOrder(10.00, 5, Side.BID);

    ExecutionResult cancelBidOrder = orderbook.cancelOrder(order.getOrderId());
    assertEquals(Status.CANCELLED, cancelBidOrder.getStatus());
  }

  @Test
  @DisplayName("ASK is cancelled and returns its orderId and a CANCELLED status")
  public void cancelAsk()
  {
    ExecutionResult order = orderbook.placeOrder(10.00, 5, Side.ASK);

    ExecutionResult cancelAskOrder = orderbook.cancelOrder(order.getOrderId());
    assertEquals(Status.CANCELLED, cancelAskOrder.getStatus());
  }

  @Test
  @DisplayName("Non-existing orderId is used to cancel a BID and returns the orderId and a NONE status")
  public void cancelNonExistingBid()
  {
    ExecutionResult cancelBidOrder = orderbook.cancelOrder(2);
    assertEquals(Status.NONE, cancelBidOrder.getStatus());
  }

  @Test
  @DisplayName("Non-existing orderId is used to cancel a ASK and returns the orderId and a NONE status")
  public void cancelNonExistingAsk()
  {
    ExecutionResult cancelAskOrder = orderbook.cancelOrder(3);
    assertEquals(Status.NONE, cancelAskOrder.getStatus());
  }

  @Test
  @DisplayName("All orderbook orders are cleared and should be empty when checked, orderId should not be reset.")
  public void clearOrderbook()
  {
    ExecutionResult firstOrder = orderbook.placeOrder(10.00, 5, Side.BID);
    orderbook.placeOrder(10.00, 5, Side.ASK);

    orderbook.clear();

    assertTrue(orderbook.isEmpty());
    ExecutionResult newOrder = orderbook.placeOrder(10.00, 7, Side.ASK);
    assertTrue(newOrder.getOrderId() > firstOrder.getOrderId());
  }

  @Test
  @DisplayName("Entire orderbook state is reset, all states should be at initial values or empty.")
  public void resetOrderbook()
  {
    orderbook.placeOrder(10.24, 10, Side.BID);

    orderbook.reset();

    assertTrue(orderbook.isEmpty());
    ExecutionResult newOrder = orderbook.placeOrder(10.00, 10, Side.BID);
    assertEquals(1, newOrder.getOrderId());
  }

}
