package com.weareadaptive.oms.priming;

import com.weareadaptive.oms.Order;

public class Delta
{
    DeltaType type;
    Order order;

    public Delta(final DeltaType type, final Order order)
    {
        this.type = type;
        this.order = order;
    }

    public DeltaType getType()
    {
        return type;
    }

    public void setType(DeltaType type)
    {
        this.type = type;
    }

    public Order getOrder()
    {
        return order;
    }

    public void setOrder(Order order)
    {
        this.order = order;
    }

    @Override
    public String toString()
    {
        return "Delta{" +
            "type=" + type +
            ", order=" + order +
            '}';
    }
}
