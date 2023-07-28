package com.weareadaptive.assignment_b.engine.service;

import com.weareadaptive.assignment_b.engine.UserRecordRepository;
import com.weareadaptive.assignment_b.engine.codecs.UserRecordCodec;
import com.weareadaptive.assignment_b.engine.entities.CreateUserRequest;
import com.weareadaptive.assignment_b.engine.entities.MutableCreateUserResponse;
import com.weareadaptive.assignment_b.engine.services.UserService;
import com.weareadaptive.assignment_b.engine.services.UserServiceClientProxy;
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
