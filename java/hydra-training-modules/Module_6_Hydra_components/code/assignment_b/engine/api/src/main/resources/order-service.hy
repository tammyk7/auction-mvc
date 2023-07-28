metadata = {
  "java.package": "com.weareadaptive.assignment_b.trading"
  "format.type.test": "allocated.Allocated[Name]"
}

import { Username } from "common.hy"

type OrderType =
  | MarketOrder = 1
  | LimitOrder = 2

type PlaceOrderRequest = {
   clientOrderId: char[20]
   user: Username
   orderType: OrderType
   price: long?
}

type ErrorResponse = {
   message: string
}

type PlaceOrderResponse =
  | Success
  | Failure of ErrorResponse

service OrderService = {
    placeOrder(PlaceOrderRequest): PlaceOrderResponse
}
