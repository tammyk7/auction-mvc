metadata = {
  "java.package": "com.weareadaptive.chatroom"
  "format.type.test": "allocated.Allocated[Name]"
}

import * from "classpath:platform/prelude.hy"

type InternalUserPermission: int64 =
   | Unauthenticated             = 0b0000_0000_0000_0000_0000

type EchoRequest = {
    body: string
}

type EchoResponse = {
    body: string
}

type HelloRequest = {
    body: string
}

type HelloResponse = {
    body: string
}

service EchoService = {
    @AccessControl(permission: InternalUserPermission.Unauthenticated)
    echo(EchoRequest): EchoResponse

    @AccessControl(permission: InternalUserPermission.Unauthenticated)
    hello(HelloRequest): HelloResponse
}