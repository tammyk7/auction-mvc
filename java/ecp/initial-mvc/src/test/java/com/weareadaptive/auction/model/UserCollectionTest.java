package com.weareadaptive.auction.model;

import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.weareadaptive.auction.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserCollectionTest
{
    private UserCollection state;

    @BeforeEach
    public void initState()
    {
        state = new UserCollection();
        Stream.of(
                ADMIN,
                USER1,
                USER2,
                USER3,
                USER4
        ).forEach(u -> state.add(u));
        state.setNextId(USER4.getId());
    }

    @Test
    @DisplayName("Should contain usernames of added users")
    public void containsUsername()
    {
        assertTrue(state.containsUsername(USER1.getUsername()));
        assertFalse(state.containsUsername("nonexistentUser"));
    }

    @Test
    @DisplayName("Should throw when adding a user with an existing username")
    public void onAddExistingUsername()
    {
        AuthenticationExceptionHandling.BusinessException thrown = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> state.add(USER1));
        assertEquals(UserCollection.ITEM_ALREADY_EXISTS, thrown.getMessage());
    }
}
