# Module 3 - Determinism & Single-Threaded Performance

Goal: To ensure people are aware of how to code for a deterministic, single-threaded system

## Why determinism is important

Deterministic code ensures consistent and predictable behavior across your code.

- Determinism refers to the property of a system where given the same inputs and initial conditions, the system will always produce the same outputs and follow the same execution path.

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

- How to write deterministic code

## Single-threaded business logic
Concurrency is a popular topic nowadays in the field of software development. However, it's important to note that not all programs can benefit from concurrency, and it may not always be the best solution depending on the specific requirements of the program— especially when building a high-speed, deterministic distributed system.

### Why is single-threaded business logic deterministic?
As we have discussed in prior modules, deterministic behavior ensures that the system is able to recover from system failures and remain consistent across all nodes. Additionally, deterministic behavior enables the system to be debugged and tested more easily, since the behavior of the system is consistent and predictable. Furthermore, Raft specifically requires that the leader's log be replicated across all nodes, and that the nodes agree on the order in which entries are added to the log. Therefore, since using concurrent business logic in this context can lead to inconsistencies and race conditions, the integrity of the system can be compromised and can lead to conflicting logs. To prevent this, it is important to use single-threaded business logic when building a deterministic, distributed system, ensuring that the system remains consistent and predictable across all nodes.

### Why can single-threaded business logic be faster than multi-threaded business logic in distributed systems?
In 1993, Intel released the first Pentium processor, which had a clock speed of 60 MHz and could process about 100 million instructions per second. Since then, processors have only improved. Given the significant increase in processing power available today, developers may want to consider utilizing this speed by implementing concurrent programs that can take advantage of the multiple cores offered on many modern CPUs.

Using concurrency can improve performance in some regards by enabling multiple tasks to be executed simultaneously, but as mentioned above, can also cause unpredictable behavior, data inconsistencies, and race conditions. Locking or compare-and-swap (CAS) operations can ensure monotonicity and avoid these problems, but may also slow down a program. This is especially significant in distributed systems, which involve multiple machines communicating with each other over a network. In these systems, acquiring locks and checking memory locations can take even longer, leading to even greater performance issues. To avoid these issues, developers should use single-threaded business logic and avoid locking and CAS operations entirely, allowing systems to take full advantage of high-speed CPUs.

### Does this mean that nodes cannot execute multiple tasks simultaneously at all?
No, nodes can still execute multiple tasks simultaneously. Instead of using multiple threads on multiple cores, single-threaded asynchronous processing can be used to allow nodes to execute multiple tasks at the same time.

In single-threaded asynchronous processing, multiple tasks are executed simultaneously without blocking the main thread of execution. However, unlike multi-threaded asynchronous processing, single-threaded asynchronous processing does not use multiple cores; rather, it uses callbacks or events to notify the program when a task has completed so that the program can continue to execute other tasks while waiting for slow I/O operations or other tasks to complete. This avoids the risk of data inconsistencies or race conditions that can occur with concurrent programming, while still allowing nodes to execute multiple tasks.

- Performance

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
The usual implementation consists in creating a factory that will create the flyweight objects, and one or multiple clients that will requests the flyweight objects from the factory.

You can find a code example in the `code` folder.

- Working with time
- Journalling and replayability

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
