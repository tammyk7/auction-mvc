package com.weareadaptive.auction.service;

import static java.lang.String.format;

import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.repository.UserRepository;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
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
      if (exception.getCause().getClass().equals(ConstraintViolationException.class)) {
        var constraint = (ConstraintViolationException) exception.getCause();

        if (constraint.getConstraintName().equals("auction_user_username_key")) {
          throw new BusinessException(format("Username %s already exist", username));
        }
      }
    }
    return user;
  }

  public Optional<User> validateUsernamePassword(String username, String password) {
    var user = userRepository.findByUsername(username);

    if (user.isEmpty() || !user.get().validatePassword(password)) {
      return Optional.empty();
    }
    return user;
  }

  public Optional<User> getUser(int userId) {
    return userRepository.findById(userId);
  }

  @Transactional
  public void block(int userId) {
    throwIfNotFoundIfNoResult(() -> userRepository.block(userId));
  }

  @Transactional
  public void unblock(int userId) {
    throwIfNotFoundIfNoResult(() -> userRepository.unblock(userId));
  }

  @Transactional
  public User updateUser(int id, String firstName, String lastName, String organisation) {
    var user = userRepository.getById(id);

    user.setFirstName(firstName);
    user.setOrganisation(organisation);
    user.setLastName(lastName);
    userRepository.save(user);
    return user;
  }

  public Stream<User> getAll() {
    return userRepository.findAll().stream();
  }

  private void throwIfNotFoundIfNoResult(Supplier<Integer> job) {
    var result = job.get();

    if (result == 0) {
      throw new NotFoundException();
    }
  }
}
