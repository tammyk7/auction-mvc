# Module 5 - Hydra Services & Code Generation

Goal: To ensure proficiency with hy-lang and designing Hydra platform services

## How to send/receive messages

In this example, the following cluster and web-gateway are created to communicate with each other, where the engine
hosts a service called EchoService.

```fsharp
cluster Engine = {
    services: {
        EchoService
    }
}
```

```fsharp
client EchoGateway = {
    connectsTo: Engine
}
```

Below, we have our `EchoService` defined in `echo.hy`. Using the command `./gradlew generateCodecs`, the hydra gradle plugin generates some classes that we will use.

```fsharp
service EchoService = {
    echoFireAndForget()
    echoFireAndForgetWithMessage(Echo)
    echoWithReply(EchoRequest): EchoResponse
    echoRespondManyTimes(EchoRequest): EchoResponse stream
    echoToEverybody: Echo stream
}
```

In the following text, we will show how to implement these classes for the most commonly used interaction models as well
as how to implement client to client communication.

### Fire-and-forget

#### Sending a message

For a service client to communicate with a service, it must access the service proxy. This is done by accessing the
corresponding channel where the service is located, and retrieving the service proxy. In the context of our example,
that can be achieved using the following line of code, where the context is the bootstrapper for our gateway.

```
final EchoServiceProxy echoServiceProxy = context.channelToCluster().getEchoServiceProxy();
```

This `EchoServiceProxy` will have a method for each method offered in the `.hy` file for `EchoService`. In order to use
the fire-and-forget method `echoFireAndForget` of `EchoService`, a correlation ID will need to be allocated for
this specific message. This can be done using the `EchoServiceProxy`’s `allocateCorrelationId()` method. Therefore,
sending a message can be achieved in the following code snippet:

```
final EchoServiceProxy echoServiceProxy = context.channelToCluster().getEchoServiceProxy();
final UniqueId correlationId = echoServiceProxy.allocateCorrelationId();
echoServiceProxy.echoFireAndForget(correlationId);
```

#### Sending an object with a message

In some cases, it may be necessary to send an object with additional information to the service, as seen
in `echoFireAndForgetWithMessage(Echo)`. In order to do this, the `Echo` object will also need to be accessed from
the `EchoServiceProxy` in addition to the `UniqueId`. Note that only accessing the `Echo` object must be done from a
try-with-resources block because of Hydra’s use of flyweights— flyweights implement `Closable` and therefore must be
closed once they are done being used.

```
try (var echo = echoServiceProxy.acquireEcho())
{
    final UniqueId correlationId1 = echoServiceProxy.allocateCorrelationId();
    echo.message("Hello from fire-and-forget!");
    echoServiceProxy.echoFireAndForgetWithMessage(correlationId1, echo);
}
```

#### Receiving the message

This message must then be received by the cluster. As is, the message is sent to the cluster and will be received by the
ingress. However, to properly handle any of the incoming messages, a proper service class will need to be implemented in
the cluster. Thanks to Hydra’s code generation, we are given a `EchoService` interface that we can implement and therefore handle
the incoming messages.

In the case of `echoFireAndForget` and `echoFireAndForgetWithMessage`, the methods that we will need to override will be
the following:

```
public class EchoServiceImpl implements EchoService
{
    @Override
    public void echoFireAndForget(final UniqueId correlationId)
    {
        // potentially state-changing logic here
    }

    @Override
    public void echoFireAndForgetWithMessage(final UniqueId correlationId, final Echo echo)
    {
        // potentially state-changing logic handling Echo object here
    }
}
```

In order to have our cluster actually use this service implementation, it should be instantiated and registered with the
cluster bootstrapper using the following lines of code.

```
final EchoServiceImpl echoService = new EchoServiceImpl();
context.channelToClients().registerEchoService(echoService);
```

Now, you may be asking— what if we want our cluster to respond to the message?

### Request-response

As described in the section on interaction models, request-response is similar to fire-and-forget in how the client
contacts a service— however, in request-response, the service responds to the request.

In our `EchoService` example, `echo(EchoRequest)` is an example of a request-response interaction model. Sending a
message from the client to the service is identical to the fire-and-forget section, as mentioned, but to respond to the
request, we will need a `EchoServiceClientProxy` to contact our clients. Similar to how we originally acquired the `EchoServiceProxy` from
the `channelToCluster().getEchoServiceProxy()` method, we will now be able to access the `EchoServiceClientProxy`.
We pass this client proxy as a constructor argument of the `EchoServiceImpl` class because its request-response method implementations
will need it to send the response back to the client.

```
final EchoServiceImpl echoService = new EchoServiceImpl(context.channelToClients().getEchoServiceClientProxy());
context.channelToClients().registerEchoService(echoService);
```

Now, in order to use this proxy to send messages to the client, we will use the
`EchoServiceClientProxy`’s `onEchoWithReplyResponse` method. This method takes a correlation ID and, because the method
responds with an `EchoResponse` object, an `EchoResponse`.

This correlation ID must match up with the correlation ID of the message sent to the service in order to complete the
interaction. If a new correlation ID is generated from the service side, the interaction will not be completed and your
tests may fail. Furthermore, like when accessing the `Echo` object in [this](#sending-an-object-with-a-message) section,
the `EchoResponse` object must be acquired from a try-with-resources block. Therefore, in the simplest case where we
want to send echo the message sent to the service, the implementation would look like the following—

```
public class EchoServiceImpl implements EchoService
{
    private final EchoServiceClientProxy echoServiceClientProxy;

    public EchoServiceImpl(EchoServiceClientProxy echoServiceClientProxy)
    {
        this.echoServiceClientProxy = echoServiceClientProxy;
    }

    @Override
    public void echoWithReply(final UniqueId correlationId, final EchoRequest echoRequest)
    {
    try (var echoResponse = echoServiceClientProxy.acquireEchoResponse())
        {
            echoResponse.message(echoRequest.message() + " This is a reply from the service!");
            echoServiceClientProxy.onEchoWithReplyResponse(correlationId, echoResponse);
        }
    }

    // other method implementations
}
```

#### Receiving a response from the cluster

In order to receive responses from a service, a client for the given service must be registered. In order to keep the
EchoGateway code clean, a new class `EchoServiceClientImpl.java` should be created that implements the Hydra-generated
class `EchoServiceClient`:

```
context.channelToCluster().registerEchoServiceClient(new EchoServiceClientImpl());
```

For a service that contains a request-response method, the service’s client interface will automatically have generated
a method that will be named `onNameOfMethodResponse`. When implementing a client, this method will need to be defined
and the response will need to be handled as needed. As the client sends out a response to the request, the response will
arrive and be handled by this method. In the case of our `echoWithReply` method, it would look like the following:

```
@Override
public void onEchoWithReplyResponse(final UniqueId id, final EchoResponse echoResponse)
{
    // logic to handle resulting response
}
```

#### Requested streams

How about sending multiple messages as a response to a client’s message in the case of requested streams, such as
with `EchoService`’s `EchoRespondManyTimes` method?

Like when sending an individual message, sending a messages over a non-broadcast stream requires a proxy (whether it
must be a client proxy or a service proxy is dependent on where the message is coming from and being sent to), a
correlation ID, and the mutable version of the message type intended to be sent over to the cluster. However, there are
some differences when sending messages in a request stream or a bidirectional stream compared to fire-and-forget,
request-response.

In the table below, you’ll find the methods that can be used with the proxy when sending messages over a stream, whether
it's from the cluster to a client or a client to the cluster. In all of these cases, if the stream is a response, the
correlationId will need to match the one of the original message. For information on these methods,
go [here](https://docs.hydra.weareadaptive.com/LATEST/Development/Services/InteractionModels.html#requested-stream).

| Method generated                                             | Use case                                                                                                                                                                                                                                                 |
|--------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| onNameOfServiceMethod(correlationId, requestType)            | Used to send RequestType object over stream with given correlationId.                                                                                                                                                                                    |
| onNameOfServiceMethodError(correlationId, errorNotification) | Used to inform the recipient that an error has occurred on the client side for that given method. More information on errors can be found https://docs.hydra.weareadaptive.com/LATEST/Development/Services/InteractionModels.html#errors.                |
| onNameOfServiceMethodCompleted(correlationId)                | Used to inform the recipient that the stream that was sending over messages has now been completed and that no other messages will be passed through that stream. If this is a bi-directional stream, this does closes the stream only in one direction. |

In the case of `echoRespondManyTimes` in our `EchoService`, sending three messages into the stream and then completing
it. If `onEchoRespondManyTimesResponseCompleted` was not used, the stream would remain open and additional messages
could be sent.

```
@Override
public void echoRespondManyTimes(final UniqueId correlationId, final EchoRequest echoRequest)
{
    LOGGER.info("Received following Echo message from echoRespondManyTimes: ".concat(echoRequest.message().toString())).log();
    try (final MutableEchoResponse echoResponse = echoServiceClientProxy.acquireEchoResponse())
    {
        for (int i = 0; i < 3; i++)
        {
            echoResponse.message(echoRequest.message() + " This is a stream reply from the service!");
            echoServiceClientProxy.onEchoRespondManyTimesResponse(correlationId, echoResponse);
        }
    }
    echoServiceClientProxy.onEchoRespondManyTimesResponseCompleted(correlationId);
}
```

### How to receive responses from a stream

To accept messages from a stream, additional methods will need to be implemented in our `EchoServiceImpl.java` class.
Any of the following can be implemented in order to provide behavior for our client in response to receiving a response
from the service.

| Method name                                                                   | Use case                                                                                       |
|-------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| onNameOfStreamResponse(final UniqueId id, final ResponseType response)        | Implement client behavior to handle object sent over the stream by a responding service        |
| onNameOfStreamErrorResponse(final UniqueId id, final ErrorNotification error) | Implement client behavior to handle Error Notification that occurred in the responding service |
| onNameOfStreamCompleted(final UniqueId id)                                    | Implement client behavior for when stream sent by the service has been completed               |

In the case of our echoRespondManyTimes method, the client-side handling of response would look like the following:

```
@Override
public void onEchoRespondManyTimesResponse(UniqueId correlationId, EchoResponse echoResponse)
{
    LOGGER.info("Received EchoResponse from echoRespondManyTimes: ".concat(echoResponse.message().toString())).log();
}

@Override
public void onEchoRespondManyTimesResponseCompleted(UniqueId correlationId)
{
    LOGGER.info("Received echoRespondManyTimes completion").log();
}
```

Note that the client can cancel an incoming stream using the service proxy’s `cancelEchoRespondManyTimes` method, given
the corresponding correlation ID. For more information on stream cancellation from the recipient side,
go [here](https://docs.hydra.weareadaptive.com/LATEST/Development/Services/InteractionModels.html#stream-cancellation).

#### How to use broadcast streams

These streams, unlike other kinds of stream-based messaging, are always infinite, cannot be cancelled by the recipient,
and do not need a correlation ID. In the case of `echoToEverybody`, it is currently configured to send out a broadcast
message upon receiving an `echoFireAndForget` message using the following code—

```
try (final MutableEcho echo = echoServiceClientProxy.acquireEcho())
{
    echoServiceClientProxy.onEchoToEverybody(echo.message("This Echo broadcast message was triggered by the echoFireAndForget"));
}
```

### How to send messages between clients

Cluster bypass has been covered in a prior section— the process of sending messages to and from the `EchoService`
provided
in the `EchoGateway` is similar as described above, and an example of it can be found
in `java/com/weareadaptive/echo/client/PeerClientMain.java`.

```fsharp
client PeerClient = {
    connectsTo: {
        Engine
        EchoGateway
    }
}
```

When running the code sample, notice that the `PeerClient` is able to use the `EchoService` without using the cluster—
hence it being cluster bypass messaging— because a separate `EchoService` was registered for the peer channel in
the `EchoGateway` bootstrapper. The `PeerClient` sends a message to the `EchoGateway` and is received by the
`EchoService` registered in `EchoGateway`, which then responds and the response is logged in the `PeerClient`.

## Understanding generated service code

- `*Driver`: The 'Context' is an object present in both the engine and the various components and can give access to
  various services
  such as:
  - channelToCluster: This is a channel from the component to the cluster, it allows the component to send messages to
    the
    cluster
  - channelToWebSockets: This is a channel from the component to the various applications connected through
    websockets. It
      allows the component to reply to connected clients.
    - channelToInitiators: Same as the previous one but for FIX connections
    - replayChannelToCluster: Create a channel that can replay all the messages for the consumer. This is useful during
      priming on startup.

  For more info you can check [here](https://docs.hydra.weareadaptive.com/LATEST/Development/Services/UnderstandingGeneratedServiceCodeBasics.html)
- `*ServiceProxy`: These classes are useful to communicate with the Service they represent.
- `*ServiceClientProxy`: Like the `ServiceProxy` ones, they are used to send a response from the Service to the client
- `Mutable*` : Every generated data class has a mutable and immutable version of it. The mutable ones have the same name
  as the immutable but with the `Mutable` prefix.
- `Test*`: It's a version of the data class that directly allocate space in the heap without taking advantage of
  the [Flyweight pattern](../Module_3_Determinism_and_single_threaded_performance/Module%203.md#flyweight-pattern).

## Interaction models
In the [Module 1 Streaming API](../Module_1_Distributed_system_basics#streaming-apis) section we described the different interaction models between clients and Hydra services, listed here:

- Fire and forget: when a response is not expected
- Request and response: when a single response is expected
- Streaming based: when either the request or the response are a stream

(refer to [Hydra doc](https://docs.hydra.weareadaptive.com/LATEST/Development/Services/InteractionModels.html) for more details).

In Hydra the call to a service is asynchronous (i.e. non-blocking), allowing the caller to perform other tasks immediately after the method call.
Unless the service method is a fire and forget (which doesn't have a response), its response must be handled in a different method (implementing the method of the [NAME_OF_SERVICE]ServiceClient interface that handles
the result).

In order to facilitate relating the method that handles the response to the request that did call the service method,
Hydra API uses the same "correlationId" value which was passed when calling the method in the call to the service client method
implementation.

## Backwards compatible services

backward compatibility ensures that newer versions of a service can coexist with previous versions without breaking existing functionality. Key aspects include:

- **Preserving Functionality**: Existing features should remain unchanged.
- **Handling Deprecated Features**: Clear communication and support for deprecated features.
- **Versioning**: Incremental versioning to indicate compatibility.
- **Testing and Validation**: Rigorous testing to avoid regressions.
- **Documentation and Communication**: Clear documentation and communication of changes.

Maintaining backward compatibility eases adoption of updates, reduces costs, and enhances user experience.

#### Changing the contract
In response to new requirements or an urge to refactor, we might need to change the service contract. Hydra can track changes to the service contract over time if we annotate the service as a "migration root".
```hydra
@MigrationRoot(id: "Calculator")
service CalculatorService = {
  sum(Terms): Result
}
```
Upon annotating the service, the code generator will check that we do not accidentally break the compatibility of the service contract.
_For example, if we change _**sum**_ to accept a stream of **Terms**_:
```hydra
@MigrationRoot(id: "Calculator")
service CalculatorService = {
  sum(Terms stream): Result
}
```

#### Types of deployment restriction
The kinds of change we can make to a service contract depends, in part, on how we intend to deploy our initiator and acceptor:
- **Same version**: If we always deploy the initiator and acceptor simultaneously.
- **Acceptor ahead by one**: If we always upgrade the acceptor first but at most one version ahead of the initiator.
- **Acceptor ahead by N**: If we always upgrade the acceptor before the initiator.
- **Initiator ahead by one**: If we always upgrade the initiator first but at most one version ahead of the acceptor.
- **Initiator ahead by N**: If we always upgrade the initiator before the acceptor.
- **Either ahead by one**: If we always upgrade either the initiator or the acceptor at most one version ahead of the other.
- **None**: If we can deploy the initiator and acceptor with any version of the contract, the acceptor must understand all versions of the request and the initiator must understand all versions of the response.


For further reference, Hydra documentation includes sections on
[Evolve a Service Contract](https://docs.hydra.weareadaptive.com/LATEST/Development/Services/Versioning/EvolveAServiceContract.html)




## How to Define Service Contracts

What is a service contract? A service contract refers to a set of rules and specifications that define interactions and
behaviors of services within a system. It acts as a contract between the service provider and the service consumer,
establishing a clear understanding of how the services should be used, what inputs are required, what outputs can be
expected, and any constraints or guarantees associated with the service.

To create a service contract with Hydra, we first need to familiarize ourselves with Hydra Language (hy-lang); a
domain-specific language that will allow you to define the structure of your application. Hy-lang provides an
abstraction over high performance tooling (i.e. Aeron, in-memory persistence, etc.) through code generation, enabling
developers to focus on business logic.

- **Types**: The first part in defining a service contract is declaring what inputs are required, and what outputs are
  expected. We can achieve this by creating a complex type called a record. A record is composed of a number of typed
  fields. These typed fields can be primitives, enums, unions, and other records. For more information on how to create
  records as well as other types,
  visit [here](https://docs.hydra.weareadaptive.com/LATEST/Development/CodeGen/HyLangTypeReference.html).

```
type MyRequest = {
   id: int32
   foo: Foo
   bar: Bar
}
```

- **Interaction Models**: After defining the types that will be used for an endpoint, let's declare how we want the
  service provider and service consumer to interact with each other. Hydra supports a myriad of interaction models such
  as fire-and-forget, request-and-response, requested stream, request stream, etc. Each of these interaction models can
  be defined through Hydra as service methods. To find out more about additional interaction models and how to define
  them as services methods
  click [here](https://docs.hydra.weareadaptive.com/LATEST/Development/Services/InteractionModels.html).

```
foo(MyRequest)                            //fire-and-forget
foo(MyRequest): MyResponse                //request-and-response
foo(MyRequest): MyResponse stream         //requested stream
foo(MyRequest stream): MyResponse         //request stream
foo(MyRequest stream): MyResponse stream  //bi-directional stream
foo: MyEvent stream                       //broadcast stream
```

- **Services**: Next, we can define our service methods in our service. A service is basically a collection of service
  methods. To properly define a service
  click [here](https://docs.hydra.weareadaptive.com/LATEST/Development/CodeGen/HyLangForServices.html).

```
service MyService = {
   foo(MyRequest): MyResponse
   bar(): Bar stream
}
```

- **Components**: Now that we’ve defined our service, we can declare that a component implements that service.
  Components are the main building blocks of a Hydra Platform application. There are many different types of components
  such as a cluster, web-gateway, fix-acceptor, fix-initiator, client, etc. For more information on components
  click [here](https://docs.hydra.weareadaptive.com/LATEST/Development/CodeGen/HyLangForComponents.html).

```
cluster MyEngine = {
   services: {
     MyService
   }
}

web-gateway MyGateway = {
   connectsTo: { MyEngine }
   services: {
     MyService
   }
}
```

## Passthrough Services

A passthrough service typically refers to a type of service that acts as intermediary or proxy, allowing data or
requests to pass through it without modifying or processing them. It simply forwards the data from one point to another,
without any manipulation.

In the context of Hydra, to send data to the cluster it must first come from a cluster client. Web-gateways (a type of
cluster client) often implements validation, processing, or mutation of data prior to passing data to the cluster.
However, what if you want to pass data to the cluster without any of this logic in a web-gateway? Hydra web-gateways
support this in a feature
called [pass-through](https://docs.hydra.weareadaptive.com/LATEST/Development/GettingStarted/Tutorial/Exercises/Passthrough.html).
This feature allows you to register a Hydra generated service proxy or client proxy rather than your own implementation
to handle an message, effectively simplifying your code.

## Hydra Tooling and IDE Plugin

### [hydra](https://docs.hydra.weareadaptive.com/LATEST/Operation/Docker/ToolsAndUtilities.html#hydra): A CLI tool for managing a local hydra instance

- **cat**: Outputs ingress/egress streams in human-readable form.
- **cluster**: General purpose cluster management. Allows exports of recovery packages, truncation of the log, setting
  of the cluster ineligibility flag. Additionally, it has monitoring tools that output the state of the cluster and
  lists leadership terms in the cluster recording log.
- **recordings**: Interact with recordings in the archive. Outputs a list of the recordings as well as recording
  details. Also allows for modification of a recording, enabling the user to attach, detach, purge, and truncate
  segments of a recording.
- **snapshots**: Interact with snapshots. Allows for importing/exporting, injecting, listing, and triggering snapshots.

### [Maintenance Scripts](https://docs.hydra.weareadaptive.com/LATEST/Operation/Docker/ToolsAndUtilities.html#maintenancesh)

- **maintenance.sh**: A script that is called for scheduled maintenance. It executes a couple of other maintenance
  scripts (maintenance_snapshot.sh, maintenance_export.sh, maintenance_archive.sh, etc.) some of which execute according
  to certain flags.
- **maintenance_snapshot.sh**: Requests that the cluster takes a snapshot, however this is only valid on the leader
  node. If executed on a follower node, will only report information on whether the snapshot was a success/failure.
- **maintenance_export.sh**: Calls an internal Hydra library to create an export (backup) in a ZIP file that can be used
  to restore the system/cluster. The export can be copied out of a docker container and used to replay all the commands
  in a local cluster node.
- **maintenance_archive.sh**: Calls an internal Hydra library to identify journal files that are no longer needed, due
  to snapshots. These journals are then zipped, to save disc space.
- **maintenance_backup.sh**: If configured, sends exports and archived journals to AWS S3.
- **maintenance_cleanup.sh**: If configured, removes all exports and archived journals that are older than a set amount
  of days.
- **maintenance_restore.sh**: Can take a hydra export from different sources (i.e. local file, HTTP, S3 URL) and primes
  the node to start with this data. Old data is moved to a separate folder. All nodes need to be restored in this manner
  and will finally be started afterward with `supervisorctl start all`.

### [IntelliJ Plugin](https://docs.hydra.weareadaptive.com/LATEST/tools/IntelliJ.html)

- Hydra provides an IntelliJ plugin to assist development of hydra platform projects, providing syntax highlighting and
  code navigation support for .hy files.
