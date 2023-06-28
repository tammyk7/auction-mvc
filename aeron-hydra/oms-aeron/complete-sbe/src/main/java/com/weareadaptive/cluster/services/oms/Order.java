package com.weareadaptive.cluster.services.oms;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import com.weareadaptive.cluster.services.oms.util.Side;

public class Order implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    private long orderId;
    private double price;
    private long size;
    private Side side;

    public Order(final long orderId, final double price, final long size, final Side side)
    {
        this.orderId = orderId;
        this.price = price;
        this.size = size;
        this.side = side;
    }

    public long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(final long orderId)
    {
        this.orderId = orderId;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(final double price)
    {
        this.price = price;
    }

    public long getSize()
    {
        return size;
    }

    public void setSize(final long size)
    {
        this.size = size;
    }

    public Side getSide()
    {
        return side;
    }

    public void setSide(final Side side)
    {
        this.side = side;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(orderId, side, price, size);
    }

    @Override
    public String toString()
    {
        return "Order{" +
            "orderId=" + orderId +
            ", price=" + price +
            ", size=" + size +
            ", side=" + side +
            '}';
    }

    @Serial
    private void writeObject(final java.io.ObjectOutputStream out) throws IOException
    {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
    }
}
