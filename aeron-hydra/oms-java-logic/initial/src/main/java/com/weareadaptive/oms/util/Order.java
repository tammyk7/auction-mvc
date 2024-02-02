package com.weareadaptive.oms.util;

public class Order
{
  private final long id;


  private final double price;

  private final long size;

  private final Side side;

  public Order(final long id, final double price, final long size, final Side side)
  {
    this.id = id;
    this.price = price;
    this.size = size;
    this.side = side;
  }

  public long getId()
  {
    return id;
  }

  public void cancelOrder()
  {
  }

  public double getPrice()
  {
    return price;
  }

  public long getSize()
  {
    return size;
  }

  public Side getSide()
  {
    return side;
  }


}
