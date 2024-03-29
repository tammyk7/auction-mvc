# Module 3 - Determinism & Single-Threaded Performance

Goal: To ensure people are aware of how to code for a deterministic, single-threaded system

<details>
<summary>Contents</summary>

- [Why determinism is important](#why-determinism-is-important)
- [How to write deterministic code](#how-to-write-deterministic-code)
- [Single-threaded business logic](#single-threaded-business-logic)
- [Zero-copy & flyweight pattern](#zero-copy--flyweight-pattern)
  - [Kernel space](#kernel-space)
  - [User space](#user-space)
  - [Flyweight pattern](#flyweight-pattern)
- [Working with time](#working-with-time)
  - [How does SystemTime cause non-deterministic behavior?](#how-does-systemtime-cause-non-deterministic-behavior)
  - [How does Aeron cluster use timers in a deterministic way?](#how-does-aeron-cluster-use-timers-in-a-deterministic-way)
- [Journaling and replayability](#journaling-and-replayability)
  - [Why have replayability?](#why-have-replayability)
  - [Does our architecture consider journals the source of truth?](#does-our-architecture-consider-journals-the-source-of-truth)
- [Performance](#performance)
- [Things to watch out for](#things-to-watch-out-for)

</details>

## Why determinism is important

Deterministic code ensures consistent and predictable behavior across your code.

- Determinism refers to the property of a system where given the same inputs and initial conditions, the system will always produce the same outputs and will be in the same final state.

Here's why determinism is important in Aeron Cluster:

Consensus Algorithm:
- Aeron Cluster uses a consensus algorithm to achieve fault-tolerant replicated state machines. Determinism ensures that all nodes in the cluster reach the same decisions and state updates. If different nodes diverge from the same inputs, consensus cannot be achieved, leading to inconsistencies and potential cluster failures.

State Replication:
- Aeron Cluster replicates the state machine across multiple nodes, ensuring that the state is consistent and up-to-date on each node. Determinism is critical to guarantee that the state updates are applied in the same order on all nodes, maintaining the integrity of the replicated state.

Handling Failures:
- In the presence of failures, determinism enables nodes to recover and resume consistent operation. By replaying the same sequence of inputs and following the same execution path, failed nodes can catch up with the current state of the cluster without diverging from the correct behavior.

Debugging and Testing:
- Determinism simplifies debugging and testing of Aeron Cluster applications. With deterministic behavior, issues can be more easily reproduced and analyzed. By replaying the same inputs, developers can investigate and fix problems without worrying about non-deterministic factors.

By ensuring deterministic code, the system achieves consistency, fault tolerance, and predictability.

It enables the reliable replication of state, proper handling of failures, and the ability to reason about system behavior.

## How to write deterministic code

As a general rule, for our code to be deterministic we can think of it as **deriving state only from the previous state and the command log**.
Here are some guidelines we can follow to accomplish this:

Avoid randomness:

- Randomness introduces variability in our code which makes it impossible to predict the outputs that we expect, using random number generators or inconsistent time sources can lead to unpredictable outputs. A common example is using system time, Hydra Platform provides you with a TimeSource that you can use to determine the time safely.
- Another common example is GUIDS, they are by definition non-deterministic. Often we use random numbers and GUIDs for transaction ids as they cannot be guessed in advance by users. When there is a security consideration, and users must not be able to guess the ID in advance, the random number should be generated outside the cluster using a non-deterministic source of randomness and passed in as part of a message. This guarantees that when you replay the logic, you will see the same value.

Consistent iteration through collections:

- To ensure determinism during iterations it's recommended to use sorted collections or specify a comparator for consistent order.

Implementing asynchronicity incorrectly:

-  Calling an external system (or disk, or database) directly from the model without re-entering the system through the message journal. If the model needs to query an external system, it doesn't have to log the outbound message, only the response matters and needs to be journaled.

Multi threading:

- Threads can be run in different orders at different times, this leads to our code being non-deterministic due to lack of consistency.

## Single-threaded business logic
In 1993, Intel released the first Pentium processor, which had a clock speed of 60 MHz and could process about 100 million instructions per second. Since then, processors have only improved, and have multiple cores that developers may want to use implementing concurrent programs.

As we have discussed before, deterministic behavior ensures that the system is able to recover from system failures and remain consistent across all nodes. Additionally, deterministic behavior enables the system to be debugged and tested more easily, since the behavior of the system is consistent and predictable. Furthermore, Raft specifically requires that the leader's log be replicated across all nodes, and that the nodes agree on the order in which entries are added to the log. Therefore, since using concurrent business logic in this context can lead to inconsistencies and race conditions, the integrity of the system can be compromised and can lead to conflicting logs. To prevent this, it is important to use single-threaded business logic when building a deterministic, distributed system, ensuring that the system remains consistent and predictable across all nodes.

## Zero-copy & flyweight pattern

### Zero-copy
"Zero-copy" describes a series of operations in which the CPU won't have to copy data from one place to another.
This is a very important concept in optimizing the performances of a program, by saving CPU cycles from copying data
from one place to another.
Current operating systems have specific system calls that will allow you to do zero-copy operations, such as `sendfile`
on Linux.
For example, if you have some data that you want to send it over a network, the traditional approach would involve copying the data from the original memory location to a new buffer (normally from the kernel to the user space), and then transferring that buffer over the network. The receiver on his end, will have to perform the same operation but reverted, from the user space to the kernel.
This copying operation adds overhead in terms of CPU cycles (4 cycles instead of 2), memory bandwidth, and overall system performance.
With a zero-copy method you can access directly the memory location of the data, and transfer it.
For a more in-depth explanation, you can read [this article](https://developer.ibm.com/articles/j-zerocopy/).

#### Kernel space:
Kernel space refers to the portion of memory where the operating system's kernel resides. It is a privileged area of memory that is protected and inaccessible to most user processes.
The kernel has direct access to hardware devices, and manages the resources of the computer.

#### User space:
User space is the memory area where application software and some drivers execute. It is the memory area where all the processes run, and where the applications are executed.

### Flyweight pattern
The flyweight pattern is a software design pattern that allows you to minimize the memory usage of an application by sharing as much data as possible with other similar objects, instead of storing copies in each of them.
A flyweight object normally contains intrinsic and extrinsic data. The intrinsic data is the data that is shared between the objects, while the extrinsic data is the data that is unique to each object.
The usual implementation consists in creating a factory that will create the flyweight objects, and one or multiple clients that will request the flyweight objects from the factory.

You can find a code example in the `code/flyweight` folder.

## Working with time
Using methods to get system time such as currentTimeMillis or LocalDateTime.now() creates non-deterministic behavior and divergence of state between cluster nodes. To avoid this, it's important to use a deterministic time source, when developing distributed systems.

### How does SystemTime cause non-deterministic behavior?
If the SystemTime is used when processing messages, the node may not end up in the same state as it would have been had it not been restarted. For example, consider a trading system that accepts orders only if the market is open. If a user submits an order while the market is open, the order will be accepted, and they will be notified that their order has been placed. If the system is restarted after the market closes, it will replay the log to get back into the correct state. However, if currentTimeMillis is used to determine the time, it will not accept the order, as it thinks it was submitted after market close. The system will then end up in a state where the order was rejected, even though it was originally accepted and the user was notified accordingly.

Also, different nodes might have a slightly different System `currentTime`, causing them to diverge and be in different state after processing the same command.

### How does Aeron cluster use timers in a deterministic way?
Aeron implements deterministic-safe timers. In order to do that, when a timer expires it will be appended to the log as a TimerEvent and replicated to follower nodes.  Once replicated, all nodes receive the TimerEvent and execute the necessary command like any other command. Note: The timer will be triggered only in the leader node. For more info you can check the [Cluster timers page](https://aeroncookbook.com/aeron-cluster/cluster-timers/) in the Aeron documentation and
the [Safely telling the time](https://docs.hydra.weareadaptive.com/LATEST/Development/Components/HowToSafelyTellTheTime.html#safely-telling-the-time) section of Hydra platform documentation.

## Journaling and replayability
  - **A journal**, or log, is a type of persistence solution where entries are appended and no longer modified afterward. The commands coming into the cluster are persisted in the journal of each individual state machine. Once the commands are executed they result in a certain end state of the replicated state machine.
  - If our code is deterministic, when we restart our state machine and replay the commands as they are stored in the journal then we would end up in the same exact end-state.
  - The ability to be able to replay the commands stored in the journal and end up in the desired state is called **replayability**.
#### Why have replayability?
- In the event a node fails, replaying of all the commands in the journal allows the node to get till the point where it dropped off.
- In the instance of an undesired end state, Replayability also allows for the user to be able to replay the commands in the journal one at a time in order to debug and locate exactly where and when the issue arose

#### Does our architecture consider journals the source of truth?
- Yes, our architecture follows command-sourcing principles and the Raft log records the sequence of all commands (messages) received by the cluster. From this log you can replay all commands one by one and recreate any intermediate state between the initial and the final state. How to replay the journal will be discussed in  module 4 after we discuss Snapshotting.

## Performance

In a low latency application, where minimizing response times is crucial, using a single thread can offer certain
advantages over multiple threads. Here are a few reasons why you might consider a single-threaded approach.

- **Synchronization**: In a multithreaded application, threads may contend for access to the same shared resource (i.e.
  database, file, or a location in memory). When a thread wants to access a locked section of code or a locked resource,
  it must wait until the lock is released by the thread currently holding it. This waiting and coordination among
  threads introduce additional computational overhead.


- **Context Switching**: When a thread is waiting for a lock, it may be temporarily suspended, and the CPU switches to
  another thread that can execute. This process is known as a context switch. Context switching adds overhead due to
  saving and restoring thread state, losing execution context of cached data, and interrupting the execution flow.


- **Locking Mechanism**: The actual mechanism used for locking can introduce overhead. Different locking primitives,
  such as mutexes, semaphores, or spin locks, have varying performance characteristics. These locking mechanisms may
  involve system calls or kernel involvement.


- **Out-of-order execution (OOE)**: is a performance optimization technique used in modern processors to improve
  instruction throughput and overall system performance. It allows the processor to execute instructions in an order
  that maximizes the utilization of execution resources, even if the original program order suggests a different
  sequence. This works without issues in single threaded programs, but has complications in multithreaded programs. OOE
  across multiple threads is more complex and requires additional mechanisms to ensure correctness and maintain data
  coherence, causing the application to potentially lose out on the performance optimization that OOE offers.


- **False Sharing**: occurs when multiple threads are accessing different variables that reside on the same cache line,
  causing unnecessary cache invalidations and memory transfers, leading to an additional performance overhead.


- **Simpler development**: it's easier to write non-multithreaded code.

However, depending on the business problem you are solving, adding more threads or adding more nodes (scaling
horizontally) may give you significantly better throughput. But not all problems parallelize well (See Amdahl's law). In
these cases, you get the parallel slowdown problems described here, which will impact latency.

## Things to watch out for

- **External State**: *Only use the model's current state and the command log messages.*

  Failure to respect this rule leads to non-deterministic behavior. For instance, if your model loads configuration
  files from disk, determinism will be broken. When you reload your model, the state will change if the configuration
  changes. To maintain determinism, all external state must enter the system as journaled commands.


- **Hash Codes**: The Object hashCode method is based on the native address pointer of the object in memory, or is
  implemented by assigning the object a random number, both of which are non-deterministic. Rerunning the application
  will result in objects being saved in memory at various different addresses and producing different results when
  calling hashCode. If the object is stored in a HashMap, each run will store it differently. Different results could be
  obtained via iterations, and you might unintentionally utilize a non-deterministic object as a non-pure input. To
  offer a deterministic implementation, override hashCode at all times.


- **Random number generators and GUIDs**: are by definition non-deterministic. For security reasons, random numbers and
  GUIDs are used for transaction ids since they cannot be predicated by users and are therefore more secure. Instead,
  use a deterministic random number generator, and seed it with a deterministic value, such as the time of the current
  command. This guarantees that, when you replay the logic, you will generate the same pseudo-random number.


- **Hardware floating point arithmetic**: can produce different results depending on the hardware it is running on.
  However, as of Java 17 floating point arithmetic is consistent regardless of hardware.


- **Garbage collector callbacks**: perform actions before or after garbage collection which are typically
  non-deterministic and therefore will behave differently if you run the system multiple times. Do not use callbacks
  like these in your model.
