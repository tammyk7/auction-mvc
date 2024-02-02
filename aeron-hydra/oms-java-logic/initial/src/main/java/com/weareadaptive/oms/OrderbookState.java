package com.weareadaptive.oms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.weareadaptive.oms.util.Order;
import com.weareadaptive.oms.util.Side;

public class OrderbookState
{
  private final TreeMap<Long, Order> bidPrice;

  private final TreeMap<Long, Order> askPrice;
  private long executionId;

  public OrderbookState()
  {
    bidPrice = new TreeMap<>((o1, o2) -> Double.compare(o2, o1));
    askPrice = new TreeMap<>();
    executionId = 0;
  }

  public Order addBid(final double price, final long size, final Side side)
  {
    ++executionId;
    final Order newOrder = new Order(executionId, price, size, side);
    bidPrice.put(executionId, newOrder);
    return newOrder;
  }

  public Order addAsk(final double price, final long size, final Side side)
  {
    ++executionId;
    final Order newOrder = new Order(executionId, price, size, side);
    askPrice.put(executionId, newOrder);
    return newOrder;
  }

  public void removeBid(final long id)
  {
    bidPrice.remove(id);
  }

  public void removeAsk(final long id)
  {
    askPrice.remove(id);
  }

  public void clearBids()
  {
    bidPrice.clear();
  }

  public void clearAsks()
  {
    askPrice.clear();
  }

  public boolean bidContains(final long id)
  {
    return bidPrice.containsKey(id);
  }

  public boolean askContains(final long id)
  {
    return askPrice.containsKey(id);
  }

  public double getBidPrice()
  {
    if (bidPrice.isEmpty())
    {
      return 0;
    }
    return bidPrice.firstEntry().getValue().getPrice();
  }

  public double getAskPrice()
  {
    if (askPrice.isEmpty())
    {
      return 0;
    }
    return askPrice.firstEntry().getValue().getPrice();
  }

  public long getBidSize()
  {
    if (bidPrice.isEmpty())
    {
      return 0;
    }
    return bidPrice.firstEntry().getValue().getSize();
  }

  public long getAskSize()
  {
    if (askPrice.isEmpty())
    {
      return 0;
    }
    return askPrice.firstEntry().getValue().getSize();
  }

  public boolean isAskPriceEmpty()
  {
    return askPrice.isEmpty();
  }

  public boolean isBidPriceEmpty()
  {
    return bidPrice.isEmpty();
  }

  public void clearExecutionId()
  {
    this.executionId = 0;
  }
}
