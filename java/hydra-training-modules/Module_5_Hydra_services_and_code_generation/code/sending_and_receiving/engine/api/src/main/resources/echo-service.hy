metadata = {
  "java.package": "com.weareadaptive.echo"
  "format.type.test": "allocated.Allocated[Name]"
}

type Echo = {
    message: char[100]
}

type EchoRequest = {
    message: char[100]
}

type EchoResponse = {
    message: char[100]
}

service EchoService = {
    echoFireAndForget()
    echoFireAndForgetWithMessage(Echo)
    echoWithReply(EchoRequest): EchoResponse
    echoRespondManyTimes(EchoRequest): EchoResponse stream
    echoToEverybody: Echo stream
}
