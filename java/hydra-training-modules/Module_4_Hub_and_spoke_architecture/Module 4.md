# Module 4 - Hub-and-spoke architecture

Goal: To ensure an understanding of how gateways interact with the cluster

## What is a hub-and-spoke architecture?

The hub-and-spoke network architecture is a networking model where a central node or “hub" serves as a central point for
communication and connects to several outlying nodes or "spokes". In this architecture, the central hub serves as the
conduit for all communication between the surrounding nodes in the system. However, we don’t purely follow this concept
as there are scenarios where it may be preferable to communicate directly with another gateway (spoke) without going
through the cluster (hub).

![](images/hub-and-spoke.png)
- Why do we need cluster clients?
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
