metadata = {
    "java.package": "com.weareadaptive.assignment_b.trading"
}

import { Engine } from "classpath:engine.hy"
import { OrderService } from "classpath:order-service.hy"

web-gateway TradingGateway = {
    connectsTo: Engine

    services: {
        OrderService
    }
}
