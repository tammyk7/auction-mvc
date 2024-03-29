# Module 4 - Hub-and-spoke architecture

Goal: To ensure an understanding of how gateways interact with the cluster

<details>
<summary>Contents</summary>

- [What is a hub-and-spoke architecture?](#what-is-a-hub-and-spoke-architecture)
- [Introduction to Hydra](#introduction-to-hydra)
- [Why do we need cluster clients?](#why-do-we-need-cluster-clients)
- [What types of cluster clients are available?](#what-types-of-cluster-clients-are-available)
- [Ingress vs. egress / inbound vs. outbound gateways](#ingress-vs-egress--inbound-vs-outbound-gateways)
- [Divergence](#divergence)
- [Cluster Bypass messaging](#cluster-bypass-messaging)
- [Snapshotting](#snapshotting)

</details>

## What is a hub-and-spoke architecture?

The hub-and-spoke network architecture is a networking model where a central node or “hub" serves as a central point for
communication and connects to several outlying nodes or "spokes". In this architecture, the central hub serves as the
conduit for all communication between the surrounding nodes in the system. However, we don’t purely follow this concept
as there are scenarios where it may be preferable to communicate directly with another gateway (spoke) without going
through the cluster (hub), see section about Cluster Bypass.
![](images/hub-and-spoke.png)

## Introduction to Hydra

[Hydra](https://docs.hydra.weareadaptive.com/LATEST/index.html) is an internal framework within Adaptive that we use to
leverage our experience in trading in finance to make it easier
and faster to build trading applications. It is built on top of Aeron cluster (i.e. therefore, a Hydra cluster also
implements the Raft distributed
consensus algorithm), and uses Aeron Transport for low latency communication between its components, which follow the
hub-and-spoke architecture.
On top of Aeron cluster and Aeron Transport, the Hydra team at Adaptive added a way to define the different components,
and how they are interconnected, and the
different service methods and API using the hydra language (hy-lang) specification.

There is a Gradle plugin
that [generates code](https://docs.hydra.weareadaptive.com/LATEST/Development/CodeGen/CodeGenOverview.html) based on the
contents
of the hy-lang files (i.e. hydra files), and you can have a look here for
a [sample of the hy-lang file](https://docs.hydra.weareadaptive.com/LATEST/Development/GettingStarted/HydraPlatformOverview.html#hydra-codecs)
,
but the specific details will be covered in *Module 5 Hydra Services and Code Generation*.

From this point on the code samples will all use Hydra. Please make sure that you have cloned
the [`onboarding` repository](https://github.com/AdaptiveConsulting/onboarding) and
successfully followed its instructions to prepare your laptop to access the Adaptive's private artifactory repository
where Hydra dependencies are located.

## Why do we need cluster clients?

*Cluster clients* act as intermediaries between incoming messages and the underlying cluster. They are essential in
distributed systems with a deterministic cluster because they provide a space for non-deterministic code and additional
computation that would otherwise burden the performance of the cluster.

In previous modules, we have stressed the importance of the logic within a cluster being deterministic. However,
non-deterministic behavior may still be necessary in a distributed system, and this type of code can be placed into a
cluster client outside of the cluster. Examples of non-deterministic code include generating random numbers and making
an external call to a 3rd party. By utilizing a cluster client, these actions can be performed and incorporated inside a
message passed to the cluster itself. This allows developers to take advantage of non-deterministic code without
directly introducing it into the cluster.

Furthermore, utilizing cluster clients also ensures that the cluster does not run non-essential code, which can affect
its performance. Running non-essential code can take up resources that are needed for essential tasks, which is why
cluster clients employ various optimization techniques to enhance overall performance. This may include validating
messages and their authentication outside of the cluster.

Cluster clients can ensure that the cluster only processes the essential tasks required to maintain high throughput and
low latency— meaning the cluster clients can take on the burden of converting messages between different kinds of
encodings. For example, a Websocket gateway can handle Websocket messages, a REST gateway can handle REST requests, and
a FIX acceptor gateway can handle FIX messages. Even having a single gateway that handles both FIX and REST requests
would still reduce the cluster's burden since message parsing is moved outside the cluster. In the picture below, each
gateway would be responsible for converting its incoming messages into the same type of command that is sent to the
cluster.![](images/different_cluster_clients.png)

## What types of cluster clients are available?

The mechanism by which components communicate with each other is through Services. You can think of components in terms
of the client-server paradigm.
From the point of view of a Hydra service, components can be:

- servers, which expose services to other components
- clients, which call services exposed by other components
- both servers (of the services they expose) and clients (of the services other components expose)

For example, a Web Gateway is a server in that it exposes services to clients on the internet, but it is also a client
in that it can call services exposed by the cluster.
A typical pattern to avoid non-deterministic behaviour in the cluster is to have the main business logic of your
application in the cluster, and interactions with anything outside of the Hydra Platform application will be done
through Gateways. There may also be other internal components, which only interact with other components within the
application.

![](images/cluster_client_types.png)

Excluding the Cluster itself, the other components in Hydra are Web Gateway, Fix Gateways (Acceptor and Initiator),
Database Writer and Integration Gateways:

- Web Gateways

The Web Gateway is a WebSocket gateway that accepts inbound connections. It is a server to external websocket clients
and is a client of Hydra services of other components. The way you define your web gateway in the Hydra language is the
following:

    web-gateway TradingGateway = {
    connectsTo: Cluster
    services: {
      PlaceOrder
      }
    }

Typically, Web Gateways will export some services and its WebSocket clients will consume them using code generated by
Hydra with efficient binary encoding on both sides.

Under the hood, the web gateway is actually multi-threaded and it converts multiple inbound WebSocket connections into a
sequence. You can learn more about the thread model
in [Hydra Documentation](https://docs.hydra.weareadaptive.com/LATEST/Development/Components/WebGatewayOverview.html#thread-model)
.

- FIX

FIX (Financial Information Exchange) is an electronic communications protocol with the purpose of exchanging financial
information.
If you need more information about the different messages in FIX have a look at the
following [link](https://fiximate.fixtrading.org/).

In Hydra, you can build either
a [FIX Initiator](https://docs.hydra.weareadaptive.com/LATEST/Development/Components/HowToBuildFixInitiator.html),
a [FIX Acceptor](https://docs.hydra.weareadaptive.com/LATEST/Development/Components/HowToBuildFixAcceptor.html). An
acceptor listens for connections and waits for counterparties to connect to it. An initiator does not listen for
connections, it actively establishes connections to counterparties. A single gateway can support only one of these
modes.

For instance, you would declare a FIX Initiator or a FIX Acceptor as follows:

    fix-dictionary MyFixDictionary = {
      dictionaryFile: "MyFixDictionary.xml"
    }

    fix-initiator MyFixInitiator = {
      connectsTo: MyCluster
      fixDictionaries: {
        MyFixDictionary
      }
    }

    fix-acceptor MyFixAcceptor = {
      connectsTo: MyCluster
      fixDictionaries: {
        MyFixDictionary
      }
    }

- Database Writer

The Database Writer is a component that receives events from the Cluster and persists them in a Database.
The component itself is agnostic of any used database. In the simplest terms, it's responsible for subscribing to
cluster events from the last persisted position.

    database-writer HistoryDatabaseWriter = {
      connectsTo: Engine
    }

See [How To Persist Events Into a Relational Database](https://docs.hydra.weareadaptive.com/LATEST/Development/Persistence/HowToPersistEventsIntoRelationalDatabase.html)
for more details.

- Integration Gateway

The standard component types that Hydra Platform provides are suitable for many common use cases, however you are likely
to have other use cases for which these standard types are not suitable. In these cases you can build an Integration
Gateway which includes an Imperative Cluster Client to allow it to communicate with the Cluster.

    client OutboundIntegrationGateway = {
      connectsTo: Engine
    }

See [How To Use The Imperative Client](https://docs.hydra.weareadaptive.com/LATEST/Development/Components/HowToUseTheImperativeClient.html)
for more details.

In the folder Code of this module you have an example of an Imperative Cluster Client, here's a guide on how to navigate it:
  - Engine Module
    - api: Here is where your hydra files live.
      - In the "engine.hy" file we defined our Cluster and passed the service we will be using.
      - In the "chat-room-service.hy" we defined the types we need to build message requests and responses, and the service we will expose.
      - Refer to the [Hydra Documentation](https://docs.hydra.weareadaptive.com/LATEST/Development/CodeGen/CodeGenOverview.html) for more information on the Hydra Language.
    - impl: Here is where we implement our code using the Generated Code from Hydra.
      - We have our EngineMain which is the entry point of the engine (a hydra single cluster node)
      - We also have our services folder that contains the implementation of the service we defined in "chat-room-service.hy"


  - Rest Module
    - api: Same as the Engine module.
      - In this case we have a "rest-gateway.hy" file, in here we will be defining what type of Cluster Client we are using and who it connects to, as described previously.
    - impl: Same as the Engine Module.
      - In this case we will be creating a REST connection using Vertx and sending a message through the MessageService Class, since we are expecting a response from the Cluster, we will also be handling the responses to the requests we make.

To test how all this is working you will follow these steps:
  1. Run the main method on EngineMain (if you get an InaccessibleObjectException, refer to the [Hydra Documentation](https://docs.hydra.weareadaptive.com/LATEST/ReleaseNotes/UpgradingToJava17.html) and follow step 4. )
  2. Run the main method on InboundIntegrationGatewayMain (If you get the same error as step 1, edit this configuration as well)
  3. To try this out with through the console write the following command:

     3.1 curl -d '{ "message": "Hello from Console" }' -H "Content-Type: application/json" -X POST http://localhost:8080/sendMessage
  4. Check the Cluster Log to see that the message has been received.

## Ingress vs. egress channels / inbound vs. outbound gateways

Ingress is the set of inbound message streams clients send to the cluster. All the messages are sequenced into the
cluster log.
Sending too many messages to the cluster can cause backpressure. Normally, if the cluster is not able to accept the
messages, they will be sent again until it can accept them.

Egress is a channel where the cluster broadcast messages to enabled clients. This is a single channel, every client will
receive all the messages and will parse them in two cases:

- The message contains the matching cluster client session id (The specific field is `targetSessionId` in the header
  message).
- The message is a broadcast.
  If a client can't process the egress at the same speed of the other clients, it will be disconnected.

Both the ingress and egress are stored to disk thanks to the archive. In case of a cluster restart, the
ingress will be replayed from the last snapshot or from the beginning of the log. The egress will query the position and
compare it with the recording end position. If they match, the new messages will be appended to the recording. If they
don't [the behaviour will depend on the set strategy](https://docs.hydra.weareadaptive.com/LATEST/HydraCluster/Egress.html#if-the-cluster-is-restarted).

The ingress and egress channel should not be confused with inbound and outbound gateways. For instance, an inbound integration gateway could call to a
cluster service request-response method. The request would be sent through the ingress channel to the cluster, and the response from the cluster would be sent back to
the gateway through the egress channel.

## Divergence

We previously stressed the importance of all the nodes behaving in a deterministic way: the outcome of applying each
command of
the log to the state machine must be the same in all the nodes, and when this is not the case, we say the nodes have
diverged.

[Hydra documentation](https://docs.hydra.weareadaptive.com/LATEST/HydraConcepts/UnderstandingDivergence.html) has a
whole section on Divergence, with several examples
which can be summarized in the advice we gave in the Module 3 section on how to write deterministic code:

- avoid randomness
- have a consistent order in collections of elements
- avoid using the node's system time (use instead Hydra's provided TimeSource)
- avoid reading state from anywhere else but the commands themselves (because that might change)

Hydra does have a [divergence monitor](https://docs.hydra.weareadaptive.com/LATEST/Operations/DivergenceMonitor.html)
that
checks the content of the egress channel and reports any divergence between a node and the leader. Sometimes, though,
the divergence
occurs but the egress might not include the "divergent" field, for instance, if random IDs were assigned to a new
instrument but the
egress channel does not include the instrument ID, then the divergence monitor would not catch this divergence, or it
might detect it much later,
only when the diverged values are included in the egress.

There is another case where the divergence monitor might not catch divergences: those caused by "snapshot-restore" bugs.
If there is some value that is not stored in a Hydra repository (i.e. some counter whose value is not persisted to a
Hydra repository
neither is set from the input command). For further reference, Hydra documentation includes sections on
[how to test for non-determinism](https://docs.hydra.weareadaptive.com/LATEST/Development/Testing/NonDeterminismTesting.html)
and
[how to test for snapshot and restore bugs](https://docs.hydra.weareadaptive.com/LATEST/Development/Testing/SnapshotRestoreTesting.html)
.

## Cluster Bypass messaging

Cluster bypassing refers to the concept of establishing direct communication between gateways within a system, bypassing
the involvement of the cluster. In this setup, the gateways form their own communication network, allowing them to
exchange messages, synchronize data, or coordinate actions directly with each other. By bypassing the cluster, gateways
can achieve faster and more efficient communication, reducing latency and overhead.

This approach is beneficial in scenarios where gateways need to exchange information quickly and efficiently without
relying on the cluster for communication. Gateways can establish direct connections among themselves, enabling seamless
communication within the gateway network. The specifics of cluster bypassing may vary depending on the system or
framework being used, but the core concept remains the same: gateways communicating with each other while bypassing the
involvement of the cluster.

For further reference, Hydra documentation includes sections on
[Cluster Bypass Messaging](https://docs.hydra.weareadaptive.com/LATEST/Development/Services/ClusterBypassCommunication.html)

## Snapshotting

Sometimes the command log can become too long and thus begin slowing down the nodes when replaying the commands. A
solution for this is to take a snapshot.

- A snapshot is a record of the entire system current state.
- When a snapshot is taken, it is encoded into messages and published to the snapshotPublication to be stored through
  the Aeron Archive.
- Now the node does not need to replay all the commands in order to get to the final state. It is able to skip over all
  the logs before the snapshot and only has to replay the commands after the snapshot.
- The snapshot saves the nodes processing time since they can jump to a certain state without having to replay all the
  commands that led the system to that state.
