metadata = {
    "java.package": "com.weareadaptive.echo.client"
}

import { EchoGateway } from "classpath:echo-gateway.hy"
import { Engine } from "classpath:engine.hy"

client PeerClient = {
    connectsTo: {
        Engine
        EchoGateway
    }
}
