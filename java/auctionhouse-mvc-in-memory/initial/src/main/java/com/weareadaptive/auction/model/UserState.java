package com.weareadaptive.auction.model;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserState extends State<User> {

  private final Map<String, User> usernameIndex;

  public UserState() {
    usernameIndex = new HashMap<>();
  }

  @Override
  protected void onAdd(User model) {
    if (usernameIndex.containsKey(model.getUsername())) {
      throw new BusinessException(format("Username \"%s\" already exist", model.getUsername()));
    }
    usernameIndex.put(model.getUsername(), model);
  }


  public Optional<User> validateUsernamePassword(String username, String password) {
    if (!usernameIndex.containsKey(username)) {
      return Optional.empty();
    }
    var user = usernameIndex.get(username);
    if (!user.validatePassword(password)) {
      return Optional.empty();
    }
    return Optional.of(user);
  }
}
