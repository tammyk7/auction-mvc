# Building an simple exchange using distributed systems

Within this project, we are striving to accomplish the following goals:
- Develop your knowledge on financial trading and how trading assets function behind the scenes.
- Cement core RAFT implementation concepts and understanding by implementing Aeron code.
- Be challenged with some intricacies of developing financial trading software.
- Build a simple exchange on a distributed system in Java using Vertx, Aeron and also optionally Hydra

## Roadmap

### Java business logic of an simple exchange

To begin this journey of building an exchange, the business logic of an exchange has to be implemented first in pure Java.

This can then be used in the further steps of the pathway as a library that contains the business logic.

Checkpoints:

- Understand the business domain of what an exchange is, how the logic works and why they are important in financial trading.
- Meet stated business requirements with passing test coverage.
- Ensure unsage of optimal data structures for sorting/searching.

### Adding a websocket interface using Vert.x

In order to interact with our exchange, we must now provide an interface such as a websocket server that can accept requests.

This would not have any persistence or fault tolerance as state is currently stored completely in-memory.

Checkpoints:

- Carry over your business logic and begin exploring how to use Vertx websockets
    - Why would we want to use Websockets and not REST?
    - Dive into the Vertx library and how to correctly test asynchronously.
- Meet stated business requirements for the websocket interface with passing test coverage.
    - Tests should take into account race conditions.
- Understand how to demultiplex/route messages.

### Building a distributed system using Aeron Cluster

Given we have our business logic complete and knowledge on how to implement a way of interfacing with our exchange logic.

In a realistic scenario our system must:
- Persist the state across restarts and failures.
- Be fault-tolerant and reliable.
- Have capable of high-throughput and low latency.

In this step, you will have to ensure the system meets all of those requirements by building a distributed system using Aeron Cluster.

This step of the roadmap will entail multiple iterations to ensure you understand the trade-offs, best practice implementation and design patterns of a system built using Aeron cluster.

**Iteration 1 - Implementing your logic within Aeron Cluster and Aeron Clients**

Checkpoints:

- Integrating business logic and websocket logic from previous steps.
- Understanding Aeron Cluster and how to implement gateways using Aeron Clients.
- Understanding Ingress/Egress messages and client sessions.
- Manual encoding and decoding of buffer messages.
- Snapshotting
- Integration & Unit Tests

**Iteration 2 - Converting your messaging to use Simple Binary Encodings**

Checkpoints:

- Convert manual decode/encoding to use Simple Binary Encodings.
- Understand how to define messages in schema.
- Use Simple Binary Encoding Headers to store metadata such as correlationId.
- Use header metadata for demultiplexing/routing messages.

**Iteration 3 - Integrating Agrona and State Machines**

Checkpoints:

- Separating project into business / infrastructure layers
- Ensure single threaded Aeron clients where possible using Agrona agents and duty cycles.
    - Publications should be exclusive to a single thread.
    - Use a state machine design pattern for each agent.

### Bonus: Rebuilding the system using Hydra Platform

Based on the collective knowledge you have gained from the previous tasks, now it should come together for you to rebuild the system using Hydra platform.

It should meet the same business and technical requirements as the previous tasks.

