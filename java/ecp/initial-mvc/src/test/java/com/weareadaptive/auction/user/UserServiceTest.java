package com.weareadaptive.auction.user;

import com.weareadaptive.auction.auction.AuctionRepository;
import com.weareadaptive.auction.bid.BidRepository;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private BidRepository bidRepository;

    @Test
    @DisplayName("Create new users")
    public void createUser()
    {
        final User user = new User(
                "username3",
                "password3",
                "First3",
                "Last3",
                "Org3");

        when(userRepository.save(any(User.class))).thenReturn(user);

        final User newUser = userService.create(
                "username3",
                "password3",
                "First3",
                "Last3",
                "Org3");

        assertEquals("username3", newUser.getUsername());
        assertEquals("First3", newUser.getFirstName());
        assertEquals("Last3", newUser.getLastName());
    }

    @Test
    @DisplayName("Get All Users")
    public void getAllUsers()
    {
        final List<User> users = Arrays.asList(
                new User("username1",
                        "password1",
                        "First1",
                        "Last1",
                        "Org1"),

                new User("username2",
                        "password2",
                        "First2",
                        "Last2",
                        "Org2")
        );

        when(userRepository.findAll())
                .thenReturn(users);

        final List<User> grabUsers = userService.getAll();

        assertEquals(2, grabUsers.size());
        assertEquals("username1", grabUsers.get(0).getUsername());
        assertEquals("username2", grabUsers.get(1).getUsername());
    }

    @Test
    @DisplayName("Get user by id")
    public void getUserById()
    {
        final User user = new User(
                "username1",
                "password1",
                "First1",
                "Last1",
                "Org1");

        when(
                userRepository.findById(1))
                .thenReturn(Optional.of(user));

        final User userReceived = userService.getUserById(1);
        assertEquals("username1", userReceived.getUsername());
    }

    @Test
    @DisplayName("Should throw an exception when getting user by a negative id")
    public void shouldThrowBusinessExceptionWhenGrabbingNonExistentUser()
    {
        when(
                userRepository.findById(-1))
                .thenReturn(Optional.empty());

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> userService.getUserById(-1));

        assertTrue(exception.getMessage().contains("user does not exist"));
    }

    @Test
    @DisplayName("Update the user status")
    public void updateUserStatus()
    {
        final User user = new User(
                "username1",
                "password1",
                "First1",
                "Last1",
                "Org1");

        when(
                userRepository.findById(1))
                .thenReturn(Optional.of(user));

        userService.updateUserStatus(1, true);

        assertTrue(userService.getUserById(1).isBlocked());
        verify(userRepository).save(user);
        assertTrue(user.isBlocked());
    }

    @Test
    @DisplayName("Should throw an exception using the wrong userId")
    public void shouldThrowBusinessExceptionWhenAttemptingToUpdateNonExistingUser()
    {
        when(
                userRepository.findById(-1))
                .thenReturn(Optional.empty());

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> userService.updateUserStatus(-1, true));

        assertTrue(exception.getMessage().contains("user does not exist"));
    }

    @Test
    @DisplayName("Find user by username")
    public void findUserByUsername()
    {
        final User user = new User(
                "username1",
                "password1",
                "First1",
                "Last1",
                "Org1");

        when(
                userRepository.findByUsername("username1"))
                .thenReturn(Optional.of(user));

        final Optional<User> userReceived = userService.findByUsername("username1");

        assertEquals("username1", userReceived.get().getUsername());
        assertEquals("First1", userReceived.get().getFirstName());
        assertEquals("Org1", userReceived.get().getOrganisation());
    }

    @Test
    @DisplayName("Should throw a business exception when using the wrong username")
    public void shouldThrowBusinessExceptionWhenFindingNonExistentUser()
    {
        when(
                userRepository.findByUsername("username123"))
                .thenReturn(Optional.empty());

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> userService.findByUsername("username123")
                        .orElseThrow(() ->
                                new AuthenticationExceptionHandling.BusinessException("user does not exist"))
        );

        assertTrue(exception.getMessage().contains("user does not exist"));
    }

    @Test
    @DisplayName("Validate username and password")
    public void validateUsernamePassword()
    {
        final User user = new User(
                "username1",
                "password1",
                "First1",
                "Last1",
                "Org1");

        when(
                userRepository.findByUsername("username1"))
                .thenReturn(Optional.of(user));

        final Optional<User> userReceived = userService.validateUsernamePassword(
                "username1",
                "password1"
        );

        assertEquals("First1", userReceived.get().getFirstName());
        assertEquals("Last1", userReceived.get().getLastName());
        assertEquals("Org1", userReceived.get().getOrganisation());
    }

    @Test
    @DisplayName("Should throw a business exception when using the wrong username")
    public void shouldThrowBusinessExceptionWhenValidatingNonExistentUser()
    {
        when(
                userRepository.findByUsername("username123"))
                .thenReturn(Optional.empty());

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> userService.validateUsernamePassword("username123", "password2")
                        .orElseThrow(() ->
                                new AuthenticationExceptionHandling.BusinessException("user does not exist"))
        );

        assertTrue(exception.getMessage().contains("user does not exist"));
    }
}
