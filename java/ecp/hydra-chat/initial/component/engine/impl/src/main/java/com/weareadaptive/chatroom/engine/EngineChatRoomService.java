package com.weareadaptive.chatroom.engine;

import com.weareadaptive.chatroom.services.ChatRoomServiceClientProxy;

/**
 * I am the implementation of the chat room service.
 * I live in the cluster
 * There is one instance of me (per cluster node) at runtime
 * I am single-threaded
 * I must be fast
 * I must be deterministic
 * Any state I maintain is derived as a result of replaying the ingress command log
 * Clients talk to me via {@link EngineChatRoomService}
 * I talk to clients via {@link ChatRoomServiceClientProxy}
 */
public class EngineChatRoomService  {

}
