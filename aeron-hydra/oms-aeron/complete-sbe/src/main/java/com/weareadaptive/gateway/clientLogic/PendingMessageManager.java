package com.weareadaptive.gateway.clientLogic;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.agrona.concurrent.EpochClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for keeping track of pending messages and their timeouts
 */
public class PendingMessageManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PendingMessageManager.class);
    private static final long TIMEOUT_MS = TimeUnit.SECONDS.toMillis(5);
    private final Deque<PendingMessage> trackedMessages = new LinkedList<>();
    private final EpochClock current;

    /**
     * Constructor
     *
     * @param current the clock to use for timeouts
     */
    public PendingMessageManager(final EpochClock current)
    {
        this.current = current;
    }

    /**
     * Add a message to the list of pending messages
     *
     * @param correlationId the correlation id of the message
     * @param messageType   the type of message
     */
    public void addMessage(final long correlationId, final String messageType)
    {
        final long timeoutAt = current.time() + TIMEOUT_MS;
        trackedMessages.add(new PendingMessage(timeoutAt, correlationId, messageType));
    }

    /**
     * Mark a message as received
     *
     * @param correlationId the correlation id of the message
     */
    public void markMessageAsReceived(final long correlationId)
    {
        trackedMessages.removeIf(pendingMessage -> pendingMessage.correlationId() == correlationId);
    }

    /**
     * Duty cycle in which the pending messages are checked for timeout; if a message is found to be timed out,
     * only a single message per duty cycle is checked.
     */
    public void doWork()
    {
        final long currentTime = current.time();
        if (null == trackedMessages.peek())
        {
            return;
        }

        //not yet at timeout
        if (currentTime < trackedMessages.peek().timeoutAt())
        {
            return;
        }

        final PendingMessage timedOut = trackedMessages.poll();

        if (null == timedOut)
        {
            return;
        }

        //after timeout
        if (currentTime >= timedOut.timeoutAt())
        {
            LOGGER.info("Message with correlation id " + timedOut.correlationId() + " and type " +
                timedOut.messageType() + " timed out.");
            trackedMessages.remove(timedOut);
        }
    }
}