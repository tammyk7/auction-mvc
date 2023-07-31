# Module 6 - Hydra Components

Goal: To embed knowledge about Hydra platform component usage

## Lifecycle events
The `Context` provides Lifecycles methods that can be used to set up or clean up your application. The methods can be divided into two groups:
- Cluster lifecycle events:
    - `onAvailable(Runnable)`: This callback will get executed every time the gateway connects to the cluster. This is a good place start the priming of your application.
    - `onUnavailable(Runnable)`: This callback will be triggered when the cluster connection is lost. This is a good place to stop incoming requests if your component is not an Hydra web-gateway.
    - `onClose(Runnable)`: This callback will be triggered when the connection with the cluster is closed. This is a good place to clean up resources.
- Component lifecycle events:
    - `notifyAvailable()`: This callback will be triggered when the component.
    - `notifyUnavailable()`: This callback will be triggered when the component
    - `notifyClosed()`: This callback will be triggered when the component is closed.

_*Note*: All the callbacks should be registered before calling the  `connect()` method. Otherwise, the callbacks might not be triggered._

Code snippet:
```java
    conn.services().lifecycle().onAvailable(() -> {
        log.info("Connected to cluster").log();
    });

    conn.services().lifecycle().onUnavailable(() -> {
        log.info("Cluster unavailable").log();
    });

    conn.services().lifecycle().onClose(() -> {
        log.info("Disconnected from cluster").log();
    });
    conn.connect();
```

- Threadsafe vs. non-threadsafe logging
- Bootstrapping components

## Assignments
We have covered a good part of Hydra functionality, it's time to put this knowledge into practice with some assignments:

- Assignment A: broadcasting events (the file [Assignment A.md](/Assignment%20A.md) has all the details)
- Assignment B: keeping a cache of users in the gateway (the file [Assignment B.md](/Assignment%20B.md) has all the details) and use it for validation checks in the gateway.

Each file has a corresponding folder under `code` (i.e. `code/assignment_a`) with some code provided, and in each
assignment it is expected that you write some code that fulfills some requirements.
