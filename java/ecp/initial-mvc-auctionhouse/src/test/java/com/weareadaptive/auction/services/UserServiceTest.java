package com.weareadaptive.auction.services;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserCollection;
import com.weareadaptive.auction.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest
{
    private UserService userService;

    @BeforeEach
    public void beforeEach()
    {
        this.userService = new UserService(new UserCollection());
        userService.create("username1", "password1", "First1",
                "Last1", "Org1");
    }

    @Test
    @DisplayName("Create new users")
    public void createUser()
    {
        final User newUser = userService.create("username3", "password3", "First3",
                "Last3", "Org3");

        assertEquals("username3", newUser.getUsername());
        assertEquals("First3", newUser.getFirstName());
        assertEquals("Last3", newUser.getLastName());
    }

    @Test
    @DisplayName("Get All Users")
    public void getAllUsers()
    {
        userService.create("username3", "password3", "First3",
                "Last3", "Org3");

        final List<User> grabUsers = userService.getAll();

        assertEquals("username1", grabUsers.get(0).getUsername());
        assertEquals("username3", grabUsers.get(1).getUsername());
    }

    @Test
    @DisplayName("Get user by id")
    public void getUserById()
    {
        final User user = userService.getUserById(1);
        assertEquals("username1", user.getUsername());
    }

    @Test
    @DisplayName("Should throw an exception when getting user by a negative id")
    public void shouldThrowBusinessExceptionWhenGrabbingUserByNegativeId()
    {
        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.getUserById(-1));
        assertTrue(exception.getMessage().contains("user does not exist"));
    }

    @Test
    @DisplayName("Update the user status")
    public void updateUserStatus()
    {
        userService.updateUserStatus(1, true);
        assertTrue(userService.getUserById(1).isBlocked());
    }

    @Test
    @DisplayName("Should throw an exception using the wrong userId")
    public void shouldThrowBusinessExceptionWhenAttemptingToUpdateNonExistingUser()
    {
        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.updateUserStatus(-1, true));
        assertTrue(exception.getMessage().contains("user does not exist"));
    }

    @Test
    @DisplayName("Find user by username")
    public void findUserByUsername()
    {
        final Optional<User> user = userService.findByUsername("username1");

        assertEquals("username1", user.get().getUsername());
        assertEquals("First1", user.get().getFirstName());
        assertEquals("Org1", user.get().getOrganisation());
    }

    @Test
    @DisplayName("Should throw a business exception when using the wrong username")
    public void shouldThrowBusinessExceptionWhenFindingNonExistentUser()
    {
        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.findByUsername("username123"));
        assertTrue(exception.getMessage().contains("user does not exist"));
    }

    @Test
    @DisplayName("Validate username and password")
    public void validateUsernamePassword()
    {
        final Optional<User> user = userService.validateUsernamePassword("username1", "password1");

        assertEquals("First1", user.get().getFirstName());
        assertEquals("Last1", user.get().getLastName());
        assertEquals("Org1", user.get().getOrganisation());
    }

    @Test
    @DisplayName("Should throw a business exception when using the wrong username")
    public void shouldThrowBusinessExceptionWhenValidatingNonExistentUser()
    {
        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.validateUsernamePassword("username123", "password2"));
        assertTrue(exception.getMessage().contains("user does not exist"));
    }
}
