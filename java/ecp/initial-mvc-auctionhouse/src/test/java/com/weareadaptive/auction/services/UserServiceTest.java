package com.weareadaptive.auction.services;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserCollection;
import com.weareadaptive.auction.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest
{

    @Autowired
    private UserService userService;
    @MockBean
    private UserCollection userCollection;


    @BeforeEach
    public void beforeEach()
    {
        final List<User> users = List.of(
                new User(1, "username1", "password", "First1", "Last1", "Org1"),
                new User(2, "username2", "password", "First2", "Last2", "Org2")
        );
        when(userCollection.stream()).thenReturn(users.stream());
    }

//    @Test
//    @DisplayName("Get All Users")
//    public void getAllUsers()
//    {
//        final List<User> grabUsers = userService.getAll();
//        assertEquals("username1", grabUsers.get(0).getUsername());
//        assertEquals("username2", grabUsers.get(1).getUsername());
//    }


}
