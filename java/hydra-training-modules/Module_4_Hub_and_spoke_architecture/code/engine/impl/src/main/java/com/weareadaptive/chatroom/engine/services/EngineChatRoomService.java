package com.weareadaptive.chatroom.engine.services;

import com.weareadaptive.chatroom.entities.MessageRequest;
import com.weareadaptive.chatroom.entities.MutableMessage;
import com.weareadaptive.chatroom.entities.MutableMessageResponse;
import com.weareadaptive.chatroom.services.ChatRoomService;
import com.weareadaptive.chatroom.services.ChatRoomServiceClientProxy;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

public class EngineChatRoomService implements ChatRoomService
{

    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(EngineChatRoomService.class);
    private final ChatRoomServiceClientProxy clientProxy;

    public EngineChatRoomService(final ChatRoomServiceClientProxy clientProxy)
    {

        this.clientProxy = clientProxy;
    }

    @Override
    public void handleMessage(final UniqueId correlationId, final MessageRequest messageRequest)
    {
        try (final MutableMessageResponse response = clientProxy.acquireMessageResponse())
        {
            try (final MutableMessage message = clientProxy.acquireMessage())
            {
                {
                    // Build request based on requirements
                    message.message(messageRequest.message());
                    // Send to event handler for the subscriber to receive
                    clientProxy.onChatRoomMessages(message);
                    LOGGER.info("Message received: ").append(message.message()).log();

                    response.success();
                    clientProxy.onHandleMessageResponse(correlationId, response);
                }
            }
        }
    }
}
