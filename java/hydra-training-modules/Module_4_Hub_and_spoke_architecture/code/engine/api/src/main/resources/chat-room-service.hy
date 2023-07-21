metadata = {
  "java.package": "com.weareadaptive.chatroom"
  "format.type.test": "allocated.Allocated[Name]"
}

type Message = {
    message: string
}

type MessageRequest = {
    message: string
}

type MessageResponse =
    | Success
    | Failure

service ChatRoomService = {
    handleMessage(MessageRequest): MessageResponse
    chatRoomMessages: Message stream
}