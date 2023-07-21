metadata = {
    "java.package": "com.weareadaptive.chatroom.rest"
}

import { Engine } from "classpath:engine.hy"

client InboundIntegrationGateway = {
    connectsTo: Engine
}