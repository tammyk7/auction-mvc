# Module 1 - Distributed system basics
Goal: To cover important distributed system fundamentals people may not be familiar with.

- RPC
- Message Passing
- Proxies and stubs
- Aeron messaging & UDP
- Media driver, etc.
- Message delivery guarantees (at-least-onceexactly-once, etc.)
- Streaming APIs (finite vs infinite streams, etc.)
- Websockets for UIs

### UDP

[UDP (User Datagram Protocol)](https://en.wikipedia.org/wiki/User_Datagram_Protocol), is a internet protocol. It is used for sending messages, or datagrams, over a network.

- UDP is a connectionless protocol, meaning that it does not establish a connection before sending data.

- It just sends the data out without ensuring that it reaches its destination.

- This is in contrast to the [TCP (Transmission Control Protocol)](https://en.wikipedia.org/wiki/Transmission_Control_Protocol), which is connection-oriented and ensures that data is received by the destination before sending more data.

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