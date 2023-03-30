metadata = {
  "java.package": "com.weareadaptive.chatroom.wg"
}

import { Engine } from "classpath:engine.hy"
import { EchoService } from "classpath:echo-service.hy"


web-gateway WebGateway = {
    connectsTo: {
        Engine
    }
    services: {
        EchoService
    }
}