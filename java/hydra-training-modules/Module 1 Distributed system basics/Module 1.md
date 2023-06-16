# Module 1 - Distributed system basics
Goal: To cover important distributed system fundamentals people may not be familiar with.

- RPC
- Message Passing
- Proxies and stubs
- Aeron messaging & UDP
- Media driver, etc.
- Message delivery guarantees (at-least-onceexactly-once, etc.)

## Streaming APIs
When a service defines its methods, there are several "interaction models" between a client (ie. caller) and
the implementation of that service depending on whether the client expects a response, and also on whether the arguments and/or responses are
single, or a stream of elements. 

Based on the above criteria, we can classify the interaction models as:

- Fire and forget: the client calls a method, optionally passing arguments to it, but does not expect nor wait any response. 
- Request and response: the client calls a method (optionally passing arguments to it) and expects to receive a response back
- Stream-based: either the method accepts a stream argument, returns a stream response, or both. 
    When the stream is an argument, each element in the stream is processed as soon as is available, whereas when the stream is a response, each element is published as soon as possible.
    
Also, a service method can be: 
- blocking (ie. synchronous): the client waits for a response 
- non-blocking (asynchronous): the client does not need to wait for a response and can define a callback method to handle the response when is received

In plain Java, any method with a `void` return type qualifies as **blocking** and **fire and forget**:

`public void doSomething(String argument)`

whereas a method that does have some return type would be classified as **blocking** and **request and response**:

`public String doSomething(String argument)`

How could we write a method in Java that accepts a stream as argument, and provides a stream as a response? 
One could **wrongly** use Java [Stream API](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html) (added in version 8) 
to write methods like these:

`public void doSomething(Stream<String> argument)`

`public Stream<String> doSomething()`

`public Stream<String> doSomething(Stream<String> argument)`

but these are not different from the **Request and response** approach above, because the code of those methods need to have all the elements
of the Stream argument by the time it processes them, and similarly they need to have all the elements of the Stream they return before the method ends.

In order to implement a truly **stream-based** approach in plain Java we can use the [Flow.Subscriber](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Flow.Subscriber.html) 
and [Flow.Publisher](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Flow.Publisher.html) 
interfaces. 

With this approach, there are 2 sides in each stream: 
- the "publisher", which accepts subscriptions from subscribers in its implementation of the only method defined in the interface **Flow.Publisher**
    `void subscribe(Flow.Subscriber<? super T> subscriber)`, and keeps track of them all. When the publisher has a new item of the stream, 
    iteratively calls each subscriber's `onNext(T item)` method. It also calls each subscriber's `onComplete()` after the last item has been published,
    as well as each subscriber's `onError(Throwable throwable)` when an error occurs.
- the "subscriber", which implements the logic of `void onNext(T item);`, `void onComplete();` and `void onError(Throwable throwable);`

Notice that there can be more than one publisher and subscriber implementing classes for the same stream.




## Websockets for UIs
