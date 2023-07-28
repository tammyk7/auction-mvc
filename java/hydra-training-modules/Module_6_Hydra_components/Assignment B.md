# Assignment B: implementing a cache of users in the trading gateway

In order to start this assignment, you must have completed the previous Assignment A, implementing
all the functionality required for Assignment A, as this will be the starting point for this assignment.

First of all, using a http client (i.e. [Postman](https://www.postman.com/)), try sending the same request
to create a new user two or more times. Does the cluster create two `UserRecord` when they have
the same username? Why?


If you answered "No, because the `username` is the primary key and can't be repeated", you are right.

The first behaviour you will have to implement in this assignment is:

- The CreateUserResponse should be changed to include the possibility of a successful or a failed user creation,
   and the method implementation in the cluster modified to avoid calling `UserRecordRepository.create()` when
   the `username` already exists. The `admin` gateway should report a 400 BAD REQUEST http response in this case.


We provide an OrderServiceImpl in the **cluster**, which implements the OrderService described in [order-service.hy](code/assignment_b/engine/api/src/main/resources/order-service.hy)

- Implement a "pass through" OrderService in the `trading` gateway that "passes through"
  the PlaceOrderRequest to the cluster's implementation of the OrderService (which we provide).
  The log entry "PlaceOrderRequest processed in the cluster" will be written if the PlaceOrderRequest reaches the cluster.


Did you notice that the PlaceOrderRequest has a `user` field? We should check that the user does exist before sending the
PlaceOrderRequest to the cluster. In the previous assignment (A) you implemented a UserServiceClient in the `trading` gateway
(we provide an implementation in this assignment, the UserServiceClientImpl) that receives each UserEvent broadcasted.

- Rename the class `UserServiceClientImpl` to `UserCache` and make sure you save the `username` of each UserEvent in a cache.
- Write a `TradingGatewayOrderServiceImpl` class that implements OrderService (defined in [order-service.hy](code/assignment_b/trading/api/src/main/resources/order-service.hy)), and register it (instead of the pass-through that you
  implemented in the `trading` gateway in the previous step). This class must validate that the `user` field in the `PlaceOrderRequest` exists in the cache
  before sending the `PlaceOrderRequest` to the cluster. If the user does not exist, reject the PlaceOrderRequest and do not send it to the cluster.
