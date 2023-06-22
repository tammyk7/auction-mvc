# Module 2 - Replicated state machines & distributed consensus

Goal: To ensure a solid understanding of distributed consensus-based systems

## State machines and the need for replication
Think for example in the software managing your bank account: at any given time, you want it to always be in exactly
one state (your bank account has either a positive, zero or negative balance, but not more than one at once),
you want it to change its state in a predictable way (ie. if you withdraw some amount, you know the balance
you expect to have after the withdrawal) and you want it to continue operating in case of some failure.

Software running in finance should have these desirable properties:
- Definite: the application must be in only one state at any given time.
- Deterministic: the application must change its state in a predictable way given its current state and input.
- [Fault-tolerant](https://en.wikipedia.org/wiki/Fault_tolerance): the application must continue to operate properly in the event of a failure

[Deterministic state machines](https://en.wikipedia.org/wiki/Deterministic_finite_automaton) are software programs that:
- at any given time, it can be in exactly one state out of a finite number of possible states
- it can change from one state to another in response to some input
- transitions from one state to another in a predictable way based only on the input and current state,
  which does not include any randomness.

Deterministic state machines have the first two properties (definite and deterministic), and in order to achieve fault-tolerance,
we can run multiple instances of the same deterministic state machine, and pass the same **sequence** of inputs
to each instance. After executing any particular input, we expect every instance to be in the same state
(because they are deterministic). A fault occurred if some instance is in a different state than the other replicas.

When a fault occurs, which state is correct and which one is wrong? If we have two replicas and each has a different state after
running the same input, we would not be able to tell which has the correct state, and which the wrong state, therefore we need
at least 3 replicas, and would assume that the single replica that differs in state is the faulty one.

Run the `Main` class in folder `code/state_machines` to see an example demonstrating two state machines almost identical, a
deterministic state machine and a faulty state machine that sometimes skips one state transition.

## Total ordering architectures
In the previous section about state machines, we mentioned that in order to achieve fault-tolerance, we could 
have several replicas of a deterministic state machine, and pass them a **sequence** of inputs to each. It is thus
critical to have all the inputs that the state machines will process **sorted in a sequence**, but in a distributed
system we could have inputs arriving from different sources, how can we sort them?

There are several [approaches to "total ordering"](https://aeroncookbook.com/distributed-systems-basics/total-ordering/):
- atomic broadcast using a sequencer: There is a sequencer service that does receive the requests and imposes an order (i.e. sets
    a sequence number to each request). The replicas only process "ordered" requests (those having the sequence number set),
    and do so without skipping any request.
- distributed consensus: only one replica receives the requests (the "leader") which requests the other replicas 
    ("follower" replicas) to replicate the request. Only when a consensus of all replicas acknowledge having replicated
    the request is the request processed.
- external ordered queues: an external service (Kafka, or a message broker) receives all the requests and orders them,
    placing them in a queue that is persisted.

- Consensus algorithms (Raft)
- Log replication
- Aeron Cluster
- Command sourcing vs. event sourcing
