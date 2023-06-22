package com.weareadaptive.streaming_apis;

import java.util.concurrent.Flow;

import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

public class Subscriber implements Flow.Subscriber<String>
{
    private final Logger logger = LoggerFactory.getThreadSafeLogger(Subscriber.class);
    private final String name;

    public Subscriber(final String name)
    {
        this.name = name;
    }

    @Override
    public void onSubscribe(final Flow.Subscription subscription)
    {
        // TODO implement
    }

    @Override
    public void onNext(final String item)
    {
        logger.info("subscriber: ").append(name).append(" received: ").append(item).log();
    }

    @Override
    public void onError(final Throwable throwable)
    {
        logger.error("subscriber: ").append(name).append(" received error:").append(throwable.getMessage()).log();
    }

    @Override
    public void onComplete()
    {
        logger.info("subscriber: ").append(name).append(" received completion signal").log();
    }
}
