package com.weareadaptive.cluster.services.oms;

import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.cluster.services.oms.util.Status;

public class OrderbookImpl implements IOrderbook
{
    @Override
    public ExecutionResult placeOrder(final double price, final long size, final Side side)
    {
        /**
         * * Implement Place Order logic
         *  - Resting orders if prices do not cross
         *  - Matching orders if prices do cross
         *  - Returns orderId and status (RESTING, PARTIAL, FILLED)
         */
        return new ExecutionResult(0, Status.NONE);
    }

    @Override
    public ExecutionResult cancelOrder(final long orderId)
    {
        /**
         * * Implement Cancel Order logic
         *  - Cancels order provided the orderId
         *  - Returns orderId and status (CANCELLED, NONE)
         */
        return new ExecutionResult(0, Status.NONE);
    }

    @Override
    public void clear()
    {
        /**
         * * Implement Clear orderbook logic
         *  - Should clear all orders
         *  - Retain orderId state
         */
    }

    @Override
    public void reset()
    {
        /**
         * * Implement Reset orderbook logic
         *  - Should clear all orders
         *  - Reset orderId state
         *  - All states should be reset
         */
    }
}
