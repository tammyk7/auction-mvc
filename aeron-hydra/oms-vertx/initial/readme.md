
# Building an Java Matching Engine - WORK IN PROGRESS

## Requirements:

Using the plain Java implementation of your Orderbook, you will now add a Vertx websocket interface. 

Within ```WebSocketServer``` you should implement the logic routing websocket requests to the corresponding Orderbook logic and return the appropriate response to the websocket client.

### Tests
- Each business scenario should have corresponding tests.
- The base required websocket tests are in ```WebsocketTest```, you should add more coverage if required.

### Notes
- You may have to tangle with the Vertx event loop, you should read into this and establish understanding.
- You can use [Postman](https://www.postman.com/) to send requests to your Vertx websocket server.

## Starter Topics:
- Matching Engines & Orderbooks
- Vertx.io

## Bonus Topics:
- Websocket request that returns sorted bids (price descending) and asks (price ascending) in JSON format.


## Documentation:
- [Vertx](https://vertx.io/docs/vertx-core/java/#_websockets)
- [Matching Engine and Orderbooks](https://weareadaptive.atlassian.net/wiki/spaces/KB/pages/3709566997/Exchanges+Matching+Engines#Orders)