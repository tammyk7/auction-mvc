package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/* Indicates that this class is a controller for a RESTful web service. It tells Spring that this
class will handle HTTP requests and return responses as JSON or XML l*/
@RestController
/* Sets a base URL path for all the endpoints in this controller. In this case, it specifies that
all the endpoints in this controller should start with "/users". */
@RequestMapping("/users")
public class UserController
{
    UserService userService;

    public UserController(final UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/")
    //The ResponseEntity is used to control the HTTP response, including the status code and body.
    public ResponseEntity<Map<String, Object>> getAllUsers()
    {
        final Map<String, Object> response = new HashMap<>();
        response.put("users", userService.getAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findByUserId(@PathVariable final int userId)
    {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<String> blockByUsername(@PathVariable final int userId,
                                                  @RequestBody final HashMap<String, Boolean> body)
    {
        userService.updateUserStatus(userId, body.get("isBlocked"));
        return ResponseEntity.ok().body("Update successful");
    }
}
