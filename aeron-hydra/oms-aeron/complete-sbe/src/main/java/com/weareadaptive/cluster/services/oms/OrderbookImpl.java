package com.weareadaptive.cluster.services.oms;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.cluster.services.oms.util.Status;

public class OrderbookImpl implements Orderbook
{

    private HashMap<Long, Order> orders = new HashMap<Long, Order>();
    private TreeSet<Order> bids = new TreeSet<>(new BidComparator());
    private TreeSet<Order> asks = new TreeSet<>(new BidComparator().reversed());
    private long orderId = 1;

    /**
     * * Implement Place Order logic
     * - Resting orders if prices do not cross
     * - Matching orders if prices do cross
     * - Returns orderId and status (RESTING, PARTIAL, FILLED)
     */
    @Override
    public ExecutionResult placeOrder(final double price, final long size, final Side side)
    {
        final Order newOrder = new Order(orderId, price, size, side);
        orders.put(orderId, newOrder);
        if (newOrder.getSide() == Side.BID)
        {
            bids.add(newOrder);
        }
        else
        {
            asks.add(newOrder);
        }

        final Status status = orderMatch(side);

        orderId++;
        return new ExecutionResult(newOrder.getOrderId(), status);
    }

    private Status orderMatch(final Side side)
    {
        final boolean isBid = side == Side.BID;
        Status flag = Status.RESTING;

        while (!bids.isEmpty() && !asks.isEmpty() && bids.first().getPrice() >= asks.first().getPrice())
        {
            final Order firstBid = bids.first();
            final Order firstAsk = asks.first();
            long diff = firstBid.getSize() - firstAsk.getSize();
            boolean isFirstOrderBigger = diff > 0;
            boolean areOrdersEqual = diff == 0;

            if (areOrdersEqual)
            {
                match(firstBid);
                match(firstAsk);
                flag = Status.FILLED;
            }
            else
            {
                final Order biggerOrder = isFirstOrderBigger ? firstBid : firstAsk;
                final Order smallerOrder = isFirstOrderBigger ? firstAsk : firstBid;

                match(smallerOrder);
                biggerOrder.setSize(Math.abs(diff));

                flag = isFirstOrderBigger == isBid ? Status.PARTIAL : Status.FILLED;
            }
        }

        return flag;
    }

    private void match(final Order o)
    {
        if (o.getSide() == Side.BID)
        {
            bids.remove(o);
        }
        else
        {
            asks.remove(o);
        }
    }

    /**
     * * Implement Cancel Order logic
     * - Cancels order provided the orderId
     * - Returns orderId and status (CANCELLED, NONE)
     */
    @Override
    public ExecutionResult cancelOrder(final long orderId)
    {
        final Order orderToRemove = orders.remove(orderId);
        if (orderToRemove != null)
        {
            final Side side = orderToRemove.getSide();
            if (side == Side.BID)
            {
                bids.remove(orderToRemove);
            }
            else
            {
                asks.remove(orderToRemove);
            }
            return new ExecutionResult(orderId, Status.CANCELLED);
        }
        else
        {
            return new ExecutionResult(orderId, Status.NONE);
        }
    }

    /**
     * * Implement Clear orderbook logic
     * - Should clear all orders
     * - Retain orderId state
     */
    @Override
    public void clear()
    {
        bids.clear();
        asks.clear();
        orders.clear();
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
        clear();
        orderId = 1;
    }

    public boolean isClear()
    {
        return bids.isEmpty() && asks.isEmpty() && orders.isEmpty();
    }

    public boolean isReset()
    {
        return isClear() && orderId == 1;
    }

    public TreeSet<Order> getBids()
    {
        return bids;
    }

    public TreeSet<Order> getAsks()
    {
        return asks;
    }

    public HashMap<Long, Order> getOrders()
    {
        return orders;
    }

    public long getOrderId()
    {
        return orderId;
    }

    public void loadState(final long orderId, final HashMap<Long, Order> orders, final TreeSet<Order> bids, final TreeSet<Order> asks)
    {
        this.orderId = orderId;
        this.orders = orders;
        this.bids = bids;
        this.asks = asks;
    }

    static class BidComparator implements Comparator<Order>, Serializable
    {
        // override the compare() method
        private static final long serialVersionUID = 1L;

        public int compare(final Order o1, final Order o2)
        {
            if (o1.getPrice() == o2.getPrice())
            {
                if (o1.getOrderId() == o2.getOrderId())
                {
                    return 0;
                }
                else
                {
                    return o1.getOrderId() > o2.getOrderId() ? 1 : -1;
                }
            }
            else if (o1.getPrice() < o2.getPrice())
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
    }
}
