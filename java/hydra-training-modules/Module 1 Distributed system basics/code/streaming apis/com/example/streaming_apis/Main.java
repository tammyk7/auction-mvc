package com.example.streaming_apis;

import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        final Random random = new Random();
        final Publisher publisher = new Publisher("Publisher");
        final Subscriber subscriberA = new Subscriber("Subscriber A");
        final Subscriber subscriberB = new Subscriber("Subscriber B");

        publisher.subscribe(subscriberA);
        publisher.subscribe(subscriberB);

        for (int i = 0; i <= random.nextInt(10); i++)
        {
            publisher.publishStreamItem();
            if (random.nextDouble(1.0d) <= 0.02d)
            {
                publisher.errorStream();
            }
        }
        publisher.completeStream();
    }
}
