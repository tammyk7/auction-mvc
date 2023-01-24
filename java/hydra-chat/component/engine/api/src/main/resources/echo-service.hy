metadata = {
  "java.package": "com.weareadaptive.chatroom"
  "format.type.test": "allocated.Allocated[Name]"
}

import * from "classpath:platform/prelude.hy"

type EchoRequest = {
    body: string
}

type EchoResponse = {
    body: string
}

service EchoService = {
    echo(EchoRequest): EchoResponse
}