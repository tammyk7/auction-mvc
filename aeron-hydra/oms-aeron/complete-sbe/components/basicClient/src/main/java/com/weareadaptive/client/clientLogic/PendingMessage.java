package com.weareadaptive.client.clientLogic;

/**
 * A message that has been sent to the cluster but has not yet been received by the client.
 */
public record PendingMessage(long timeoutAt, long correlationId, String messageType)
{
}
