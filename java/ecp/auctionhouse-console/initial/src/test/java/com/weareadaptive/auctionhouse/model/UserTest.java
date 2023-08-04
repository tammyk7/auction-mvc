package com.weareadaptive.auctionhouse.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UserTest {

  private static Stream<Arguments> createUserArguments() {
    return Stream.of(
        Arguments.of("username",
            (Executable) () -> new User(1, null, "pp", "first", "last", "Org")),
        Arguments.of("firstName",
            (Executable) () -> new User(1, "username", "pp", null, "last", "Org")),
        Arguments.of("lastName",
            (Executable) () -> new User(1, "username", "pp", "first", null, "Org")),
        Arguments.of("organisation",
            (Executable) () -> new User(1, "username", "pp", "first", "last", null)),
        Arguments.of("password",
            (Executable) () -> new User(1, "username", null, "first", "last", "Org"))
    );
  }

  @ParameterizedTest(name = "Create user should throw when {0} is null")
  @MethodSource("createUserArguments")
  public void createUserShouldThrowWhenNullProperty(String propertyName,
                                                    Executable userExecutable) {
    //Act
    var exception = assertThrows(BusinessException.class, userExecutable);

    //Assert
    assertTrue(exception.getMessage().contains(propertyName));
  }

  @Test
  @DisplayName("ValidatePassword should return false when the password is not valid")
  public void shouldReturnFalseWhenThePasswordIsNotValid() {
    //Arrange
    var user = new User(1, "test", "thepassword", "Jonh", "Doe", "Adaptive");

    //Act
    var result = user.validatePassword("bad");

    //Assert
    assertFalse(result);
  }

  @Test
  @DisplayName("ValidatePassword should return true when the password is valid")
  public void shouldReturnTrueWhenThePasswordIsValid() {
    //Arrange
    var user = new User(1, "test", "thepassword", "Jonh", "Doe", "Adaptive");

    //Act
    var result = user.validatePassword("thepassword");

    //Assert
    assertTrue(result);
  }
}
