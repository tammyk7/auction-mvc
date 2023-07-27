package com.weareadaptive.assignment_a.engine.service;

import com.weareadaptive.assignment_a.engine.UserRecordRepository;
import com.weareadaptive.assignment_a.engine.codecs.UserRecordCodec;
import com.weareadaptive.assignment_a.engine.entities.CreateUserRequest;
import com.weareadaptive.assignment_a.engine.entities.MutableCreateUserResponse;
import com.weareadaptive.assignment_a.engine.services.UserService;
import com.weareadaptive.assignment_a.engine.services.UserServiceClientProxy;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

public class UserServiceImpl implements UserService
{
    private final UserServiceClientProxy clientProxy;
    private final UserRecordRepository userRecordRepository;

    public UserServiceImpl(final UserServiceClientProxy userServiceClientProxy,
                           final UserRecordRepository userRecordRepository)
    {
        this.clientProxy = userServiceClientProxy;
        this.userRecordRepository = userRecordRepository;
    }

    @Override
    public void createUser(final UniqueId correlationId, final CreateUserRequest createUserRequest)
    {
        try (final MutableCreateUserResponse mutableCreateUserResponse = clientProxy.acquireCreateUserResponse();
             final UserRecordCodec user = userRecordRepository.create(createUserRequest.username())
                 .age(createUserRequest.age()))
        {
            clientProxy.onCreateUserResponse(correlationId, mutableCreateUserResponse);
        }
    }
}
