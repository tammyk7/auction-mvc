package com.weareadaptive.cluster.services.oms.util;

public class ExecutionResult
{
    long orderId;
    Status status;

    public ExecutionResult(final long orderId, final Status status)
    {
        this.orderId = orderId;
        this.status = status;
    }

    public long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(long orderId)
    {
        this.orderId = orderId;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
}
