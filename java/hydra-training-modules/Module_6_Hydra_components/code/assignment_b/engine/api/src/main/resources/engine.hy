metadata = {
  "java.package": "com.weareadaptive.assignment_b.engine"
}

import { UserService } from "user-service.hy"
import { OrderService } from "order-service.hy"

cluster Engine = {
    services: {
        UserService
        OrderService
    }
}
