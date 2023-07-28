package com.weareadaptive.assignment_b.trading.service;

import com.weareadaptive.assignment_b.engine.entities.UserEvent;
import com.weareadaptive.assignment_b.engine.services.UserServiceClient;
import com.weareadaptive.hydra.logging.Logger;
import com.weareadaptive.hydra.logging.LoggerFactory;

public class UserServiceClientImpl implements UserServiceClient
{
    private static final Logger LOGGER = LoggerFactory.getNotThreadSafeLogger(UserServiceClientImpl.class);

    @Override
    public void onUserEvents(final UserEvent userEvent)
    {
        userEvent.ifUserCreated(event -> LOGGER.info("Received userEvent (created):").append(userEvent.asUserCreated().toString()).log());
    }
}
