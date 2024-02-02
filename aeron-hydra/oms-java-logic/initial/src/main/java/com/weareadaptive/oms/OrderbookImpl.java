package com.weareadaptive.oms;

import java.util.List;

import com.weareadaptive.oms.util.ExecutionResult;
import com.weareadaptive.oms.util.Order;
import com.weareadaptive.oms.util.Side;
import com.weareadaptive.oms.util.Status;

public class OrderbookImpl implements Orderbook
{
  private final OrderbookState orderbookState = new OrderbookState();


  /**
   * * Implement Place Order logic
   * - Resting orders if prices do not cross
   * - Matching orders if prices do cross
   * - Returns orderId and status (RESTING, PARTIAL, FILLED)
   */

  @Override
  public ExecutionResult placeOrder(final double price, final long size, final Side side)
  {
    Order newOrder = null;
    if (side == Side.BID)
    {
      newOrder = orderbookState.addBid(price, size, side);
    }
    if (side == Side.ASK)
    {
      newOrder = orderbookState.addAsk(price, size, side);
    }

    double bestPrice = (side == Side.BID) ? orderbookState.getAskPrice() : orderbookState.getBidPrice();
    boolean isPriceEmpty = (side == Side.BID) ? orderbookState.isAskPriceEmpty() : orderbookState.isBidPriceEmpty();

    if ((side == Side.BID && price >= bestPrice && !isPriceEmpty) || (side == Side.ASK && price <= bestPrice && !isPriceEmpty))
    {
      final long availableSize = (side == Side.BID) ? orderbookState.getAskSize() : orderbookState.getBidSize();
      if (size <= availableSize)
      {
        return new ExecutionResult(newOrder.getId(), Status.FILLED);
      }
      return new ExecutionResult(newOrder.getId(), Status.PARTIAL);
    }

    return new ExecutionResult(newOrder.getId(), Status.RESTING);
  }

  /**
   * * Implement Cancel Order logic
   * - Cancels order provided the orderId
   * - Returns orderId and status (CANCELLED, NONE)
   */
  @Override
  public ExecutionResult cancelOrder(final long orderId)
  {
    if (orderbookState.bidContains(orderId))
    {
      orderbookState.removeBid(orderId);
      return new ExecutionResult(orderId, Status.CANCELLED);
    }

    if (orderbookState.askContains(orderId))
    {
      orderbookState.removeAsk(orderId);
      return new ExecutionResult(orderId, Status.CANCELLED);
    }

    return new ExecutionResult(0, Status.NONE);
  }

  /**
   * * Implement Clear orderbook logic
   * - Should clear all orders
   * - Retain orderId state
   */
  @Override
  public void clear()
  {
    orderbookState.clearBids();
    orderbookState.clearAsks();
  }

  /**
   * * Implement Reset orderbook logic
   * - Should clear all orders
   * - Reset orderId state
   * - All states should be reset
   */
  @Override
  public void reset()
  {
    orderbookState.clearBids();
    orderbookState.clearAsks();
    //clear id:
    orderbookState.clearExecutionId();
  }

  @Override
  public boolean isEmpty()
  {
    return orderbookState.isBidPriceEmpty() && orderbookState.isAskPriceEmpty();
  }
}
