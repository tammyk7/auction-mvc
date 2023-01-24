metadata = {
  "java.package": "com.weareadaptive.chatroom"
  "format.type.test": "allocated.Allocated[Name]"
}

type Message = {
  user: char[64]
  text: char[280]
}

type BroadcastChatMessageRequest = {
  message: Message
}

type ChatRoomEvent = {
  message: Message
}

service ChatRoomService = {
    broadcastMessage(BroadcastChatMessageRequest)
    last10Chats(): ChatRoomEvent stream
    chatRoomEvent: ChatRoomEvent stream
}