package com.weareadaptive.assignment_b.engine.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.weareadaptive.assignment_b.engine.UserRecordRepository;
import com.weareadaptive.assignment_b.engine.allocated.AllocatedCreateUserRequest;
import com.weareadaptive.assignment_b.engine.codecs.UserRecordCodec;
import com.weareadaptive.assignment_b.engine.entities.CreateUserResponse;
import com.weareadaptive.assignment_b.engine.entities.MutableCreateUserRequest;
import com.weareadaptive.assignment_b.engine.entities.MutableCreateUserResponse;
import com.weareadaptive.assignment_b.engine.services.UserServiceClientProxy;
import com.weareadaptive.hydra.platform.commontypes.entities.UniqueId;

import org.junit.jupiter.api.Test;


class UserServiceImplTest
{
    @Test
    void createUser()
    {
        final UserServiceClientProxy clientProxy = mock(UserServiceClientProxy.class);

        final UserRecordRepository userRecordRepository = mock(UserRecordRepository.class);
        final UserRecordCodec userRecord = mock(UserRecordCodec.class);
        when(userRecordRepository.create(any())).thenReturn(userRecord);

        final UserServiceImpl userService = new UserServiceImpl(clientProxy, userRecordRepository);

        final MutableCreateUserResponse createUserResponse = mock(MutableCreateUserResponse.class);
        when(clientProxy.acquireCreateUserResponse()).thenReturn(createUserResponse);

        final UniqueId mockedCorrelationId = mock(UniqueId.class);

        final MutableCreateUserRequest createUserRequest = new AllocatedCreateUserRequest();
        createUserRequest
            .username("username")
            .age((byte)19);

        userService.createUser(mockedCorrelationId, createUserRequest);

        // check that the call to the service does create a new user in the repository
        verify(userRecordRepository, times(1)).create(createUserRequest.username());

        // check that the call to the service does call the client proxy methods
        verify(clientProxy, times(1)).acquireCreateUserResponse();
        verify(clientProxy, times(1)).onCreateUserResponse(eq(mockedCorrelationId), any(CreateUserResponse.class));
    }
}
