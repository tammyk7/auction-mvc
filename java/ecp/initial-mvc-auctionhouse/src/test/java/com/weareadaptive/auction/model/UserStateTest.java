package com.weareadaptive.auction.model;

import com.weareadaptive.auction.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Stream;

import static com.weareadaptive.auction.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserStateTest
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
        assertTrue(state.containsUsername(TestData.USER1.getUsername()));
        assertFalse(state.containsUsername("nonexistentUser"));
    }

    @Test
    @DisplayName("Should throw when adding a user with an existing username")
    public void onAddExistingUsername()
    {
        BusinessException thrown = assertThrows(BusinessException.class,
                () -> state.add(TestData.USER1));
        assertEquals(State.ITEM_ALREADY_EXISTS, thrown.getMessage());
    }
}
