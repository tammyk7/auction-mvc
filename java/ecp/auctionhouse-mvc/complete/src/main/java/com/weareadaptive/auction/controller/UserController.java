package com.weareadaptive.auction.controller;

import static com.weareadaptive.auction.controller.UserMapper.map;
import static org.springframework.http.HttpStatus.CREATED;

import com.weareadaptive.auction.controller.dto.CreateUserRequest;
import com.weareadaptive.auction.controller.dto.UpdateUserRequest;
import com.weareadaptive.auction.controller.dto.UserResponse;
import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.service.UserService;
import jakarta.validation.Valid;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  @ResponseStatus(CREATED)
  public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
    return map(userService.create(
        request.username(),
        request.password(),
        request.firstName(),
        request.lastName(),
        request.organisation()));
  }

  @GetMapping("/{id}")
  public UserResponse get(@PathVariable int id) {
    return userService.getUser(id)
        .map(UserMapper::map)
        .orElseThrow(NotFoundException::new);
  }

  @PutMapping("/{id}")
  public UserResponse update(
      @PathVariable int id,
      @Valid @RequestBody
      UpdateUserRequest request) {
    return map(userService.updateUser(
            id,
            request.firstName(),
            request.lastName(),
            request.organisation()));
  }

  @GetMapping
  public Stream<UserResponse> getAll() {
    return userService.getAll()
        .map(UserMapper::map);
  }

  @PutMapping("/{id}/block")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void block(@PathVariable int id) {
    userService.block(id);
  }

  @PutMapping("/{id}/unblock")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unblock(@PathVariable int id) {
    userService.unblock(id);
  }
}
