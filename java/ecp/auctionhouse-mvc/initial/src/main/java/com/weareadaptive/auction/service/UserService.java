package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.repository.UserRepository;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<User> validateUsernamePassword(String username, String password) {
    var user = userRepository.findByUsername(username);

    if (user.isEmpty() || !user.get().validatePassword(password)) {
      return Optional.empty();
    }
    return user;
  }

  @Transactional
  public User create(String username, String password, String firstName, String lastName,
                     String organisation) {
    User user = new User(
        username,
        password,
        firstName,
        lastName,
        organisation,
        false);
    try {
      userRepository.save(user);
    } catch (DataIntegrityViolationException exception) {
      // todo: handle exception to return a business exception when username already exists
    }
    return user;
  }
}
