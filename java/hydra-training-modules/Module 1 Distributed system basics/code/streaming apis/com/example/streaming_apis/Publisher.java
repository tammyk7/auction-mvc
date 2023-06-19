package com.example.streaming_apis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow;

public class Publisher implements Flow.Publisher<String>
{
    private final String publisherName;
    List<Flow.Subscriber<? super String>> subscribers;

    public Publisher(final String publisherName)
    {
        this.publisherName = publisherName;
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void subscribe(final Flow.Subscriber<? super String> subscriber)
    {
        subscribers.add(subscriber);
    }

    public void publishStreamItem()
    {
        final String string = UUID.randomUUID().toString();
        for (Flow.Subscriber<? super String> subscriber : subscribers)
        {
            subscriber.onNext(string);
        }
    }

    public void completeStream()
    {
        for (Flow.Subscriber<? super String> subscriber : subscribers)
        {
            subscriber.onComplete();
        }
    }

    public void errorStream()
    {
        Throwable t = new IllegalStateException(publisherName+" threw some exception");
        for (Flow.Subscriber<? super String> subscriber : subscribers)
        {
            subscriber.onError(t);
        }
    }
}
