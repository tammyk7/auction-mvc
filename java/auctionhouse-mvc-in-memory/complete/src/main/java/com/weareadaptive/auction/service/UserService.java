package com.weareadaptive.auction.service;

import static java.util.Optional.ofNullable;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserState userState;

  public UserService(UserState userState) {
    this.userState = userState;
  }

  public User create(String username, String password, String firstName, String lastName,
                     String organisation) {
    User user = new User(
        userState.nextId(),
        username,
        password,
        firstName,
        lastName,
        organisation);
    userState.add(user);
    return user;
  }

  public Optional<User> getUser(int id) {
    return ofNullable(userState.get(id));
  }

  public Optional<User> updateUser(int id, String firstName, String lastName, String organisation) {
    var user = userState.get(id);

    if (user == null) {
      return Optional.empty();
    }

    user.setFirstName(firstName);
    user.setOrganisation(organisation);
    user.setLastName(lastName);
    return Optional.of(user);
  }

  public Stream<User> getAll() {
    return userState.stream();
  }
}
