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
- Assignment B: keeping a cache of users in the gateway (the file [Assignment B.md](/Assignment%20B.md) has all the
  details) and use it for validation checks in the gateway.

Each file has a corresponding folder under `code` (i.e. `code/assignment_a`) with some code provided, and in each
assignment it is expected that you write some code that fulfills some requirements.

## Bootstrapping Components

Previously mentioned in Module 5, components are the main building blocks of a Hydra Platform application. For each
component defined in hy files, Hydra Platform will generate two types used for starting a component. For example, let’s
define a component (cluster) in Hydra:

```
cluster Engine = {
    services: {
        EchoService
    }
}
```

When generating the codecs we get these two types:

- A component class that provides methods for starting a component.
  ```java
  public final class Engine {
    //...
    //This method would be used in a main method to start the cluster in production
    public static Configurer<ClusterNodeBinding> node(EngineBootstrapper bootstrapper, ClusterClock clusterClock) {}
    //...
  }
  ```

- A Bootstrapper interface, which you can use to define a components' behavior.
  ```java
  public interface EngineBootstrapper {
      void bootstrap(EngineContext context);
  }
  ```

These generated types will be named based on the name you have given to your component in the hy file, as seen in the
example. However, different types of components will have different methods for starting the component. Each component
will also have different methods for starting up in different contexts. For example, starting a component in a main
method in production code is different from starting the same component in a test.

Before we continue, let's create another component and a service:

- Note: This code is referenced from
  the [Hydra Platform tutorial](https://docs.hydra.weareadaptive.com/LATEST/Development/GettingStarted/Tutorial/DefiningTheApplication.html#defining-the-application).

```
web-gateway TradingWebGateway = {
    connectsTo: {
        Engine
    }
    services: {
        EchoService
    }
}

type EchoRequest = {
    body: string
}

type EchoResponse = {
    body: string
}

service EchoService = {
    echo(EchoRequest): EchoResponse
}
```
- **TradingWebGateway Generated Types**:

```java
public final class TradingWebGateway {
    //...
    //This method would be used in a main method to start the cluster in production
    public static Configurer<WebBinding> gateway(TradingWebGatewayBootstrapper bootstrapper) {
    }
    //...
}
```

```java
public interface TradingWebGatewayBootstrapper {
    void bootstrap(TradingWebGatewayContext context);
}
```

Did you notice how the method names are different for a `web-gateway`? For `web-gateway` we use a method
named `gateway`, but for the `cluster` we use a method named `node`. Each of these components have different method
names for starting up in production. These are the methods used in a main method:

- node - (Cluster nodes)
- gateway - (Gateways)
- instance - (Imperative cluster clients)
- service - (Other components that expose services internally)

To find out more about how to start up each component in the context of testing,
click [here](https://docs.hydra.weareadaptive.com/LATEST/Development/Components/HowToBootstrapComponents.html#starting-in-embedded-direct-mode-tests).

### Implementing Generated Code

Now, that we've established how Hydra generates code for each component, lets move on to implementation. Starting with
the `TradingWebGatewayBootstrapper` below, we want to register our service implementation given the `context` provided
by our bootstrapper interface. The `context` will provide methods to register each service defined in the hy files as a
part of its respective component.

These methods can be accessed through two channels:

- **TradingWebGatewayToWebClientsChannel**: `context.channelToWebSockets()`
- **ClientToEngineChannel**: `context.channelToCluster()`

In this scenario, we have an `EchoService` that we register to `channelToWebSockets` which will handle incoming
messages from a web session. Additionally, we have an `EchoServiceClient` that we register to `channelToCluster` to
handle incoming messages from the cluster.

Now that we have implemented our bootstrapper, we can now use it start up our gateway in the main method.

```java
public class TradingWebGatewayMain {
    public static final TradingWebGatewayBootstrapper BOOTSTRAPPER = context ->
    {
        final ClientToEngineChannel channelToCluster = context.channelToCluster();
        final EchoServiceProxy clusterServiceProxy = channelToCluster.getEchoServiceProxy();
        final TradingWebGatewayToWebClientsChannel channelToWebSockets = context.channelToWebSockets();
        final EchoServiceClientProxy webSocketClientProxy = channelToWebSockets.getEchoServiceClientProxy();

        channelToWebSockets.registerEchoService(new EchoService() {
            @Override
            public void echo(final UniqueId correlationId, final EchoRequest echoRequest) {
                clusterServiceProxy.echo(correlationId, echoRequest);
            }
        });

        channelToCluster.registerEchoServiceClient(new EchoServiceClient() {
            @Override
            public void onEchoResponse(final UniqueId correlationId, final EchoResponse echoResponse) {
                webSocketClientProxy.onEchoResponse(correlationId, echoResponse);
            }
        });
    };

    public static void main(String[] args) {
        try (final WebBinding webBinding = TradingWebGateway.gateway(BOOTSTRAPPER).run()) {
            webBinding.awaitShutdown();
        }
    }
}
```

Similarly, we implement our bootstrapper for the cluster in the same fashion. With the cluster we can access a channel
provided by the `context`:

- **EngineToClientsChannel**: `context.channelToClients()`

Registering our `EchoService` implementation to the `channelToClients` will allow the cluster to handle incoming
messages from our `TradingWebGateway`. After completing the implementation, we can also start up the cluster.

 ```java
 public class EngineMain {
    public static final EngineBootstrapper BOOTSTRAPPER = context ->
    {
        final EchoServiceClientProxy clientProxy = context.channelToClients().getEchoServiceClientProxy();
        context.channelToClients().registerEchoService(new EchoService() {
            @Override
            public void echo(final UniqueId correlationId, final EchoRequest echoRequest) {
                try (final MutableEchoResponse response = clientProxy.acquireEchoResponse()) {
                    response.body(echoRequest.body());
                    clientProxy.onEchoResponse(correlationId, response);
                }
            }
        });
    };

    public static void main(final String[] args) {
        try (final ClusterNodeBinding binding = Engine.node(BOOTSTRAPPER, new MillisecondClusterClock()).run()) {
            binding.awaitShutdown();
        }
    }
}
 ```

As a side note, there are additional objects that you can register under the `context` of the cluster. The most
important one being a `database`. However, this is something that will be covered in the next module **Hydra Persistence**.
