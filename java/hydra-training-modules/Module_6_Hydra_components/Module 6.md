# Module 6 - Hydra Components

Goal: To embed knowledge about Hydra platform component usage

## Bootstrapping components

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

## Thread-safe vs. Non-thread-safe logging

In Hydra, logging plays a crucial role, and the framework provides a custom logging solution to minimize unnecessary
garbage collection, which can be explored in
detail [here](https://docs.hydra.weareadaptive.com/LATEST/Development/HydraLogging/Overview.html). The implementation of
this framework utilizes fixed-size buffers.

The framework offers two logging options: a thread-safe logger and a non-thread-safe logger. In most cases, it is
recommended to use the non-thread-safe logger. The reason behind this lies in the fact that the thread-safe logger consumes
more resources, maintaining multiple buffers instead of a single buffer used by the non-thread-safe logger.
Considering [Hydra's single-threaded approach in the cluster](https://docs.hydra.weareadaptive.com/LATEST/HydraArchitecture/SingleThreadedBusinessLogic.html#warning-never-block-the-model-thread)
and [in gateways](https://docs.hydra.weareadaptive.com/LATEST/Development/Components/WebGatewayOverview.html#thread-model),
using the thread-safe logger is typically unnecessary.

Below, you can find a code snippet demonstrating how to log (i.e. an error message).

```java
public class Foo
{
    private Logger logger = LoggerFactory.getNotThreadSafeLogger(Foo.class);

    private void myMethod()
    {
        logger.error("Method not yet implemented").log();
    }
}
```

## Assignments
We have covered a good part of Hydra functionality, it's time to put this knowledge into practice with some assignments:

- Assignment A: broadcasting events (the file [Assignment A.md](/Assignment%20A.md) has all the details)
- Assignment B: keeping a cache of users in the gateway (the file [Assignment B.md](/Assignment%20B.md) has all the details) and use it for validation checks in the gateway.

Each file has a corresponding folder under `code` (i.e. `code/assignment_a`) with some code provided, and in each
assignment it is expected that you write some code that fulfills some requirements.
