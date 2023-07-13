# Module 4 - Hub-and-spoke architecture

Goal: To ensure an understanding of how gateways interact with the cluster

## What is a hub-and-spoke architecture?

The hub-and-spoke network architecture is a networking model where a central node or “hub" serves as a central point for
communication and connects to several outlying nodes or "spokes". In this architecture, the central hub serves as the
conduit for all communication between the surrounding nodes in the system. However, we don’t purely follow this concept
as there are scenarios where it may be preferable to communicate directly with another gateway (spoke) without going
through the cluster (hub).

![](images/hub-and-spoke.png)
## Why do we need cluster clients?
*Cluster clients* act as intermediaries between incoming messages and the underlying cluster. They are essential in distributed systems with a deterministic cluster because they provide a space for non-deterministic code and additional computation that would otherwise burden the performance of the cluster.

In previous modules, we have stressed the importance of the logic within a cluster being deterministic. However, non-deterministic behavior may still be necessary in a distributed system, and this type of code can be placed into a cluster client outside of the cluster. Examples of non-deterministic code include generating random numbers and making an external call to a 3rd party. By utilizing a cluster client, these actions can be performed and incorporated inside a message passed to the cluster itself. This allows developers to take advantage of non-deterministic code without directly introducing it into the cluster.

Furthermore, utilizing cluster clients also ensures that the cluster does not run non-essential code, which can affect its performance. Running non-essential code can take up resources that are needed for essential tasks, which is why cluster clients employ various optimization techniques to enhance overall performance. This may include validating messages and their authentication outside of the cluster.

Cluster clients can ensure that the cluster only processes the essential tasks required to maintain high throughput and low latency— meaning the cluster clients can take on the burden of converting messages between different kinds of encodings. For example, a REST gateway can handle REST requests, a FIX acceptor gateway can handle FIX messages, and an imperative cluster client gateway could be used to expose REST endpoints. Even having a single gateway that handles both FIX and REST requests would still reduce the cluster's burden since message parsing is moved outside the cluster. In the example below, each gateway would be responsible for converting its incoming messages into the same type of command that is sent to the cluster.![](images/different_cluster_clients.png)

- What type of cluster clients are needed / what is the role of gateways
  - Websockets
  - FIX
  - Database
  - Custom (imperative clients)
- Ingress vs. egress / inbound vs. outbound gateways
- Cluster bypass messaging
- Snapshotting

## Divergence
We previously stressed the importance of all the nodes behaving in a deterministic way: the outcome of applying each command of
the log to the state machine must be the same in all the nodes, and when this is not the case, we say the nodes have diverged.

[Hydra documentation](https://docs.hydra.weareadaptive.com/LATEST/HydraConcepts/UnderstandingDivergence.html) has a whole section on Divergence, with several examples
which can be summarized in the advice we gave in the Module 3 section on how to write deterministic code:
- avoid randomness
- have a consistent order in collections of elements
- avoid using the node's system time (use instead Hydra's provided TimeSource)
- avoid reading state from anywhere else but the commands themselves (because that might change)

Hydra does have a [divergence monitor](https://docs.hydra.weareadaptive.com/LATEST/Operations/DivergenceMonitor.html) that
checks the content of the egress channel and reports any divergence between a node and the leader. Sometimes, though, the divergence
occurs but the egress might not include the "divergent" field, for instance, if random IDs were assigned to a new instrument but the
egress channel does not include the instrument ID, then the divergence monitor would not catch this divergence, or it might detect it much later,
only when the diverged values are included in the egress.

There is another case where the divergence monitor might not catch divergences: those caused by "snapshot-restore" bugs.
If there is some value that is not stored in a Hydra repository (i.e. some counter whose value is not persisted to a Hydra repository
neither is set from the input command). For further reference, Hydra documentation includes sections on
[how to test for non-determinism](https://docs.hydra.weareadaptive.com/LATEST/Development/Testing/NonDeterminismTesting.html) and
[how to test for snapshot and restore bugs](https://docs.hydra.weareadaptive.com/LATEST/Development/Testing/SnapshotRestoreTesting.html).


## Cluster Bypass messaging

Cluster bypassing refers to the concept of establishing direct communication between gateways within a system, bypassing the involvement of the cluster. In this setup, the gateways form their own communication network, allowing them to exchange messages, synchronize data, or coordinate actions directly with each other. By bypassing the cluster, gateways can achieve faster and more efficient communication, reducing latency and overhead.

This approach is beneficial in scenarios where gateways need to exchange information quickly and efficiently without relying on the cluster for communication. Gateways can establish direct connections among themselves, enabling seamless communication within the gateway network. The specifics of cluster bypassing may vary depending on the system or framework being used, but the core concept remains the same: gateways communicating with each other while bypassing the involvement of the cluster.

For further reference, Hydra documentation includes sections on 
[Cluster Bypass Messaging](https://docs.hydra.weareadaptive.com/LATEST/Development/Services/ClusterBypassCommunication.html)
