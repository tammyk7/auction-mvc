# Module 1 - Distributed system basics
This initial module covers important concepts of distributed systems, in case backend developers are not familiar with them.

## RPC
RPC (Remote Procedure Call) is a communication protocol that allows a client to call a method on a remote server.
It facilitates communication between distinct processes or systems by enabling them to interact over a network as if they were executing operations within the same local environment.
The requesting process is called the client, and the process that provides the requested service is called the server.

Here's an overview of how RPC works:
1. The client sends the request through the client stub.
2. The client stub marshals the parameters and sends them to the server stub.
3. The server stub receives the request and unmarshals the parameters.
4. The server executes the procedure and sends the results back to the server stub.
5. The server stub marshals the results and sends them back to the client stub.
6. The client stub receives the results and unmarshals them.

_Note: Marshalling is the process of transforming the memory representation of an object to a data format suitable for storage or transmission._

[You can find a UML diagram of the RPC process here](diagrams/rpc_protocol.plantuml)

#### Some popular RPCs

| Name of RPC | Date Introduced | Pros | Cons | Specification Link |
| --- | --- | --- | --- | --- |
| gRPC | 2015 | High performance, supports multiple languages, uses HTTP/2 protocol, supports Protocol Buffers. | Learning curve for Protocol Buffers, HTTP/2 not fully supported in all environments. | [gRPC](https://grpc.io/docs/what-is-grpc/introduction/) |
| JSON-RPC | 2001 | Simple and lightweight, language agnostic, human-readable data format. | Not as efficient or robust as binary protocols, no streaming or flow control support. | [JSON-RPC](https://www.jsonrpc.org/specification) |
| XML-RPC | 1998 | Simple, language-agnostic, human-readable data format. | Verbosity of XML leads to larger payloads, not as efficient or robust as binary protocols. | [XML-RPC](http://xmlrpc.scripting.com/spec.html) |
| Java RMI | 1995 | Allows for direct method invocation, includes garbage collection of remote objects, supports dynamic class loading. | Limited to Java environments, has fallen out of favor due to the rise of web services. | [Java RMI](https://docs.oracle.com/en/java/javase/17/docs/specs/rmi/index.html) |
| SOAP | 1998 | Highly extensible, language and platform independent, supports WS-Security. | Verbosity of XML leads to larger payloads, complexity can make development and debugging difficult. | [SOAP](https://www.w3.org/TR/soap/) |
| Apache Thrift | 2007 | Supports many programming languages, efficient binary protocol, allows for synchronous and asynchronous communication. | Interface definition language can be complex to learn, less community support compared to newer protocols. | [Apache Thrift](https://thrift.apache.org/docs/idl) |
| RESTful APIs | 2000 | Simple to understand and use, leverages standard HTTP protocol, language-agnostic, large support community. | Not a binary protocol, less efficient than some other methods, lacks real-time communication support. | [REST](https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm) |



## Passing Messages in Distributed Systems

Passing messages in distributed systems is a fundamental aspect of their design and operation.
In distributed systems, where multiple independent components communicate and collaborate to achieve a common goal,
passing messages enables the exchange of information and coordination between these components.

**Message passing** is a key mechanism for communication in distributed systems. It involves sending data
or instructions from one component to another, typically over a network. Messages can take different forms,
such as method calls, events, or data packets. By exchanging messages, components can share information,
request actions, or notify each other about specific events, facilitating cooperation and synchronization
in the system.

One important consideration in passing messages is the choice of **communication paradigm**.
Distributed systems can adopt different communication paradigms, such as synchronous or asynchronous messaging.
In synchronous messaging, the sender blocks until the receiver processes the message, ensuring a direct response.
Asynchronous messaging, on the other hand, allows the sender to continue its execution without waiting for a response,
enabling greater concurrency and scalability.

**Reliable message delivery** is another crucial aspect of passing messages in distributed systems.
Since components may reside on different machines or operate under varying conditions, ensuring that messages
reach their intended recipients is essential. Techniques such as acknowledgments, retries, and message queuing
systems can be employed to enhance reliability. Additionally, protocols like TCP/IP provide reliable delivery
guarantees at the network level.

**Message serialization and deserialization** play a significant role in passing messages between components.
Serialization involves converting complex data structures or objects into a suitable format for transmission,
such as JSON or Protocol Buffers. Deserialization is the reverse process of reconstructing the original object
from the transmitted data. Proper serialization and deserialization are essential for ensuring compatibility
and integrity of the messages across different components and platforms.

**Message routing** is an important consideration when passing messages in distributed systems.
In large-scale systems, messages may need to traverse multiple intermediate nodes before reaching their destination.
Routing algorithms and protocols, such as publish-subscribe or message brokers, help guide messages efficiently
through the network, considering factors like load balancing, fault tolerance, and scalability.

Ensuring **message order and consistency** is crucial in distributed systems, where components may execute
concurrently and messages may arrive out of order. Techniques like message sequencing, logical clocks, or
distributed consensus algorithms can be employed to establish a total or partial ordering of messages and
maintain consistency across the system.

**Security and authentication** are vital aspects when passing messages in distributed systems,
as they need to ensure the confidentiality, integrity, and authenticity of the exchanged data.
Techniques such as encryption, digital signatures, and secure channels can be employed to protect messages
from unauthorized access or tampering. Furthermore, authentication mechanisms like tokens or certificates
can be used to verify the identities of the sender and receiver, preventing malicious actors from impersonating
legitimate components.


## Proxies and Stub & Skeleton

In a distributed software architecture, components interact with each other across different
machines, networks, or locations through remote procedure calls (RPC). RPC enables applications
to make method/function/procedure calls to remote applications, allowing them to access services
provided by a server. These interactions involve the client-side stubs and server-side skeletons,
which handle data marshalling and unmarshalling, simplifying communication by abstracting the
complexities of network communication and data transformation and giving the illusion that the
remote procedure is being executed locally.

The client that makes a procedure call and the server exposing the procedure endpoint both
share a service definition. A tool automatically generates code based on the definition, producing
client-side stubs and server-side that handle data marshalling and unmarshalling,
abstracting the complexities of network communication, ensuring compatibility and type-checking
at compile time.

Distributed systems using RPC face various challenges, one of them is ensuring compatibility
of RPC calls, ie. when something changes in the service definition, all its clients and the server/s
need to be updated, or the change needs to be backwards-compatible. It is this necessary to ensure
backward compatibility and have automatic tests asserting that every update to the service definition
works in a system in production having the older version of that service definition.

Automatic versioning aids in recognizing and adjusting for changes in serialised details,
but it requires access to the previous version's serialisation details for accurate comparison, therefore
all service changes need to be tracked.

When considering an RPC system, there are also security issues like permissions, encryption,
data integrity, confidentiality, privacy, and non-repudiation which sometimes are important for
business reasons and need to be considered.

## UDP

[UDP (User Datagram Protocol)](https://en.wikipedia.org/wiki/User_Datagram_Protocol), is a internet protocol. It is used for sending messages, or datagrams, over a network.

- UDP is a connectionless protocol, meaning that it does not establish a connection before sending data.

- It just sends the data out without ensuring that it reaches its destination, nor the order in which is received.

- This is in contrast to the [TCP (Transmission Control Protocol)](https://en.wikipedia.org/wiki/Transmission_Control_Protocol), which is connection-oriented and ensures that data is received by the destination before sending more data.

To see that UDP packets are not received in any particular order, run the `Main` class
in folder `code/udp`. There is a `Sender` running in a thread, that sends many UDP packets in order,
but the `Receiver` running in a different thread will likely receive them in wrong order, and throws
and exception when this happens.

### Aeron Transport:

[Aeron](https://aeron.io/) is a high-performance messaging system designed for real-time applications where low-latency communication is critical, such as financial trading systems.

Aeron uses UDP as a transport protocol for several reasons:

- Low Latency: It speeds up communications by not formally establishing a connection before data is transferred. This allows data to be transferred very quickly. This makes it well-suited for use cases where speed is essential such as high-throughput low latency trading systems.

- Multicast Support: UDP supports multicast, meaning a single packet can be sent to multiple recipients with no additional overhead. This is useful for distributing messages to multiple subscribers efficiently, which is a common pattern in messaging systems like Aeron.

- Efficient Throughput: UDP can be more efficient in terms of throughput compared to TCP, especially in high-bandwidth, low-latency environments. This is because it doesn’t have the congestion control mechanisms that can sometimes throttle TCP’s throughput.

- Simplicity and Control: UDP is simpler than TCP and gives the application more control over the transmission. This allows Aeron to implement its own reliability and flow control mechanisms on top of UDP, tuned for the specific requirements of high-performance messaging.

- Loss Tolerance: In some real-time applications, it's acceptable to lose some messages if the system is overloaded or network conditions are poor. UDP makes it easy to build systems with this kind of loss-tolerant behavior.

### Aeron Message Delivery Guarantees

However, it's important to note that because UDP itself doesn’t guarantee message delivery or order, Aeron implements its own set of features to handle message reliability, ordering, and flow control as needed to ensure **at-least-once delivery** of messages.

In summary, UDP's low-latency, multicast support, and simplicity make it an ideal transport layer for high-performance messaging systems like Aeron, which require efficient and real-time communication over networks.

### Aeron Media Driver

The [Aeron Media Driver](https://aeroncookbook.com/aeron/media-driver/) is a crucial component that manages the low-level network communication for the messaging system. It is responsible for efficiently transmitting and receiving messages over the network, usually using the UDP protocol.

- Specifically, it handles tasks such as buffering messages, sending and receiving data packets, ensuring reliable delivery through retransmissions and acknowledgments, and managing flow control to prevent overloading receivers.

We need the Media Driver because it abstracts away the complexities of network communication from the application layer. By doing this, it allows you to focus on business logic without worrying about the intricacies of network protocols, reliability, and performance.

- Furthermore, the Media Driver is optimized for high-throughput and low-latency communication, which is essential in systems where performance is critical, such as financial trading applications or real-time data streaming.
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
- the "publisher", which accepts subscriptions in its implementation of the only method defined in the interface **Flow.Publisher**
    `void subscribe(Flow.Subscriber<? super T> subscriber)`, and keeps track of them all. When the publisher has a new item of the stream,
    iteratively calls each subscriber's `onNext(T item)` method. It also calls each subscriber's `onComplete()` after the last item has been published,
    as well as each subscriber's `onError(Throwable throwable)` when an error occurs.
- the "subscriber" which is passed in the call to the publisher's `subscribe(Flow.Subscriber<? super T> subscriber)` method,  and implements the logic of `void onNext(T item);`, `void onComplete();` and `void onError(Throwable throwable);` methods defined in the interface **Flow.Subscriber**

Notice that there can be more than one publisher and subscriber implementing classes for the same stream.

Check the example code provided in folder `code/streaming_apis` by running the Main class. In this example, the Main class
registers two subscribers in the publisher, and the publisher "publishes" a random number of items by calling the
`onNext` and `onComplete` when it finishes. There's a small random likelihood that the publisher will call `onError`, run
it several times.


## REST APIs

REST APIs (Representational State Transfer Application Programming Interfaces) are a crucial component in modern software development, enabling seamless communication and data exchange between different systems and applications. Designed around the principles of simplicity, scalability, and interoperability, REST APIs provide developers with a standardized way to interact with web services.

### HTTP Protocol Foundation

REST APIs use the HTTP protocol as a foundation, making them highly accessible and widely supported across various programming languages and platforms. They utilize HTTP methods, such as GET, POST, PUT, PATCH, and DELETE, to perform operations on resources identified by unique URLs, known as endpoints. These endpoints represent different functionalities or data entities within the API.

### Key Functionality and Benefits

Developers can leverage REST APIs to build applications that consume data from remote servers or expose their own services for others to consume. By sending requests to the API's endpoints, developers can retrieve data, create new resources, update existing ones, or delete them as needed. The API responds with HTTP status codes and data payloads, typically in formats like JSON or XML, allowing for efficient data transmission and interpretation.

One of the key advantages of REST APIs is their stateless nature. Each request contains all the necessary information to process it, eliminating the need for the server to store any session-specific data. This characteristic contributes to scalability and simplifies the API design.

### Documentation and Integration

To facilitate developers' usage and adoption, REST APIs often include comprehensive documentation that outlines the available endpoints, their parameters, authentication requirements, and response structures. Additionally, many APIs provide software development kits (SDKs) or client libraries tailored to specific programming languages, easing the integration process and reducing implementation complexities.

### Authentication and Security

Authentication and authorization mechanisms play a vital role in securing REST APIs. Developers can employ techniques like API keys, OAuth, or JSON Web Tokens (JWT) to verify the identity of the clients and restrict access to sensitive resources.


## WebSockets

WebSockets are a communication protocol that enables real-time, bidirectional communication between a client and a server over a single, long-lived connection. Unlike REST APIs, which are request-response based, WebSockets provide a persistent connection that allows for continuous data exchange and instant updates.

WebSockets are particularly useful for applications that require real-time updates, such as chat applications, collaborative tools, or real-time monitoring systems. They provide efficient and low-latency communication, allowing for instant data transmission between the client and the server.

### REST vs WebSockets

WebSockets and REST APIs serve different purposes and have distinct characteristics:

- **Communication Pattern**: WebSockets enable real-time, bidirectional communication, while REST APIs follow a request-response pattern.

- **Connection Persistence**: WebSockets maintain a persistent connection, allowing for continuous data exchange, while REST APIs are stateless and do not retain connections between requests.

- **Data Transmission**: WebSockets use a binary-based message format, which is more compact and efficient for continuous data streams, while REST APIs typically use JSON or XML for data transmission.

- **Use Cases**: WebSockets are suitable for applications that require real-time updates or frequent data exchanges, such as chat applications or collaborative tools. REST APIs are commonly used for building client-server interactions and accessing resources over HTTP.

- **Scalability**: REST APIs can be easily scaled since each request is independent, whereas WebSockets require additional considerations to handle concurrent connections and manage server resources.

- **Compatibility**: REST APIs are widely supported across different platforms and programming languages due to their adherence to HTTP standards. WebSockets support is not as ubiquitous, although it is increasingly available in modern web frameworks and libraries.

Both WebSockets and REST APIs have their strengths and are suited for different use cases. Choosing between them depends on the specific requirements of your application, with WebSockets providing real-time capabilities and REST APIs offering simplicity and wide compatibility.


