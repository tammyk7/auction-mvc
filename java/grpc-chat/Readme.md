# Basic GRPC Chat Example

## Project Structure

**contract** - chat-service.proto defines contract  
**sender** - defines a client of the chat service that subscribes to messages (twice) and sends some test messages  
**receiver** - chat server that receives and broadcasts chat messages

## Building

The gradle protobuf plugin is used to generate the various stubs and services. To generate the code execute

```shell
./gradlew generateProto
```

## Running

To run the server run ReceiverMain in the receiver module. This starts a server on localhost at port 8980.

To run the client run SenderMain in the sender module. This conects to the server on localhost:8980 and sends several chat 
messages as well as printing the responses.

<!-- TODO 

Fix gradle structure: shared dependencies should be in buildSrc, etc
Better error handling
Tests?
Add to readme to explain the concepts and implementation

-->


