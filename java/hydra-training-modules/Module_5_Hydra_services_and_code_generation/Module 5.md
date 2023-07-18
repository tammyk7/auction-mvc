# Module 5 - Hydra Services & Code Generation

Goal: To ensure proficiency with hy-lang and designing Hydra platform services

- How to send/receive messages
- Understanding generated service code
- Interaction models
- Passthrough services
- Backwards and forwards compatible services
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
