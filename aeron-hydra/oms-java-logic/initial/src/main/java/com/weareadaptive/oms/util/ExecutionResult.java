package com.weareadaptive.oms.util;

public class ExecutionResult
{
  final long orderId;
  final Status status;

  public ExecutionResult(final long orderId, final Status status)
  {
    this.orderId = orderId;
    this.status = status;
  }

  public long getOrderId()
  {
    return orderId;
  }

  public Status getStatus()
  {
    return status;
  }
}
