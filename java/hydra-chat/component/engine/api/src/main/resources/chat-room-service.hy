metadata = {
  "java.package": "com.weareadaptive.chatroom"
  "format.type.test": "allocated.Allocated[Name]"
}

/// A message
type Message = {
  user: char[64]
  text: char[280]
}

/// Request to send a message to the room
type BroadcastChatMessageRequest = {
  message: Message
}

/// An event (note past-tense) indicating that a message has been received by the room
type MessageReceived = {
  message: Message
}

/// Union type of all the different chat room events that a client might receive on the chatRoomEvent stream
type ChatRoomEvent =
 | MessageReceived of MessageReceived

service ChatRoomService = {
    /// Broadcast my chat message to anybody in this room. Clients use this service to send messages to the room
    broadcastMessage(BroadcastChatMessageRequest)

    /// An infinite stream of events related to the chat room. Clients should subscribe to this stream to receive messages from the room
    chatRoomEvent: ChatRoomEvent stream
}