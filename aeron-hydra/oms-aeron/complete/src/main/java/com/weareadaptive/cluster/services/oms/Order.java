package com.weareadaptive.cluster.services.oms;

import com.weareadaptive.cluster.services.oms.util.Side;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Order implements Serializable
{

    @Serial
    private static final long serialVersionUID = 1L;
    long orderId;
    double price;
    long size;
    Side side;

    public Order(long orderId, double price, long size, Side side)
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

    public void setSize(final long size)
    {
        this.size = size;
    }

    public void setOrderId(long orderId)
    {
        this.orderId = orderId;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setSide(Side side)
    {
        this.side = side;
    }

    @Override
    public int hashCode() {
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
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
