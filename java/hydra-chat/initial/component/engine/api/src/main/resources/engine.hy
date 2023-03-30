metadata = {
  "java.package": "com.weareadaptive.chatroom.engine"
}

import { ChatRoomService } from "chat-room-service.hy"
import { EchoService } from "echo-service.hy"

/// I am the cluster definition. I define what services I want clients of the cluster to have access to
cluster Engine = {
    services: {
        ChatRoomService
        EchoService
    }
}
