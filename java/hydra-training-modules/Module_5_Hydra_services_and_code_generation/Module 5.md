# Module 5 - Hydra Services & Code Generation

Goal: To ensure proficiency with hy-lang and designing Hydra platform services

- How to send/receive messages
- Understanding generated service code

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



- Passthrough services
- Backwards and forwards compatible services

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


- Hydra Tooling and IDE plugins

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
