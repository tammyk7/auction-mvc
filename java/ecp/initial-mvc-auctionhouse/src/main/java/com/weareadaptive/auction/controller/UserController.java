package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.RequestsResponses.CreateUserRequest;
import com.weareadaptive.auction.controller.RequestsResponses.UserResponse;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{
    final UserService userService;

    public UserController(final UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<UserResponse> getAllUsers()
    {
        final List<User> users = userService.getAll();
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getOrganisation())
                ).toList();
    }

    @PostMapping("/")
    public UserResponse createUser(@RequestBody final CreateUserRequest createUserRequest)
    {
        final User userCreated = userService.create(
                createUserRequest.username(),
                createUserRequest.password(),
                createUserRequest.firstname(),
                createUserRequest.lastname(),
                createUserRequest.organisation()
        );

        return new UserResponse(
                userCreated.getId(),
                userCreated.getUsername(),
                userCreated.getFirstName(),
                userCreated.getLastName(),
                userCreated.getOrganisation()
        );
    }

    @GetMapping("/{userId}")
    public UserResponse findByUserId(@PathVariable final int userId)
    {
        final User user = userService.getUserById(userId);
        return new UserResponse(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getOrganisation()
        );
    }

    @PutMapping("/{userId}/status")
    public void blockByUsername(@PathVariable final int userId,
                                @RequestBody final HashMap<String, Boolean> body)
    {
        userService.updateUserStatus(userId, body.get("isBlocked"));
    }

    @GetMapping("/{userId}/bids")
    public List<Bid> getUserBids(@PathVariable final int userId)
    {
        final User user = userService.getUserById(userId);
        return userService.getUserBids(user.getId());
    }
}
