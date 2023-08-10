# Hydra Tutorial Trade Service

Prerequisite: Complete the [Hydra Platform Tutorial] and exercises.

[Hydra Platform Tutorial]: https://docs.hydra.weareadaptive.com/LATEST/Development/GettingStarted/Tutorial/HydraPlatformTutorial.html

## Part 1

In your hydra platform tutorial project, define and implement a new cluster service called `TradeService`, with one operation called `executeTrade` that accepts a `TradeRequest` and returns a `TradeResponse`.

The service should:

- Accept an incoming trade request. Trade requests should have a currency pair, amount (or “notional”), buy/sell direction, value date and price.
- Return an outgoing trade response to the caller. The response should have an accept or reject flag/enum, along with an execution id that represents the execution instance.
- You only need to implement the happy path, i.e. return an accept response.
- You don’t need to implement anything else. For each incoming request, just return an accept response.

Create a passing cucumber feature that proves your new service (you may be able to work it out based on the echo one).

Try to isolate your new service and request/response types in its own Hydra definition file.

## Part 2

Define a new web gateway, the “Trade Gateway” which is only for people who do trading.

It should connect to the Trade Service in the cluster.

## Part 3

Ideally, your trade response should include a trade id for the just executed trade, but this will require adding some state to the cluster to keep track of trade ids already used.

Introduce a repository to maintain the count of trade ids and add tests for this new feature.

## Part 4

Expand the trade service so that it automatically rejects trade requests that are “too large”, e.g. greater than 10 million.

Also include a test for this use case.

## Example Trade Request

Feel free to change as you see fit.

```typescript
type Direction =
  | Buy = 1
  | Sell = 2

type TradeStatus =
  | Accepted = 1
  | Rejected = 2

type TradeRequest = {
  currencyPair: string
  amount: int32
  direction: Direction
  valueDate: string
  price: float32
}

type TradeResponse = {
  id: int32
  tradeStatus: TradeStatus
}
```

## Example Cucumber Features

Feel free to change as you see fit:

```gherkin
Feature: TradeRequest
  Scenario: Expect response to a trade request
    Given a connected web session

    When the web session sends a trade request uniqueId1
      | CurrencyPair | Amount | Direction | ValueDate     | Price |
      | EURUSD       | 15     | Buy       | June 11, 2022 | 1.5   |

    Then the web session gets a trade response for uniqueId1
      | Id | Status |
      | 1  | Accepted |
```
