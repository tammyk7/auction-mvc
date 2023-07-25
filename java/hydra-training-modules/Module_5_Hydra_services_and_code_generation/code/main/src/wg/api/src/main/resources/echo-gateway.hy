metadata = {
    "java.package": "com.weareadaptive.echo.wg"
}

import { Engine } from "classpath:engine.hy"
import { EchoService } from "classpath:echo-service.hy"

web-gateway EchoGateway = {
    connectsTo: Engine

    peerServices: {
        EchoService
    }
}
