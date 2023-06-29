# Module 3 - Determinism & Single-Threaded Performance

Goal: To ensure people are aware of how to code for a deterministic, single-threaded system

- Why determinism is important
- How to write deterministic code
- Single-threaded business logic
- Performance
- Zero-garbage & flyweight pattern (working with pooled objects)
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
