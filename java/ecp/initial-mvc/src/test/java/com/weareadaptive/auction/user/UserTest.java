package com.weareadaptive.auction.user;

import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest
{

    private static Stream<Arguments> createUserArguments()
    {
        return Stream.of(
                Arguments.of("username",
                        (Executable) () -> new User(null, "pp", "first", "last", "Org")),
                Arguments.of("first name",
                        (Executable) () -> new User("username", "pp", null, "last", "Org")),
                Arguments.of("last name",
                        (Executable) () -> new User("username", "pp", "first", null, "Org")),
                Arguments.of("organisation",
                        (Executable) () -> new User("username", "pp", "first", "last", null)),
                Arguments.of("password",
                        (Executable) () -> new User("username", null, "first", "last", "Org"))
        );
    }

    @ParameterizedTest(name = "Create user should throw when {0} is null")
    @MethodSource("createUserArguments")
    public void createUserShouldThrowWhenNullProperty(final String propertyName,
                                                      final Executable userExecutable)
    {
        //Act
        var exception = assertThrows(AuthenticationExceptionHandling.BusinessException.class, userExecutable);

        //Assert
        assertTrue(exception.getMessage().contains(propertyName));
    }

    @Test
    @DisplayName("ValidatePassword should return false when the password is not valid")
    public void shouldReturnFalseWhenThePasswordIsNotValid()
    {
        //Arrange
        var user = new User("test", "thepassword", "Jonh", "Doe", "Adaptive");

        //Act
        var result = user.validatePassword("bad");

        //Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("ValidatePassword should return true when the password is valid")
    public void shouldReturnTrueWhenThePasswordIsValid()
    {
        //Arrange
        var user = new User("test", "thepassword", "Jonh", "Doe", "Adaptive");

        //Act
        var result = user.validatePassword("thepassword");

        //Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Users should update values correctly")
    public void settersShouldUpdateValues()
    {
        User user = new User("username1", "password", "John", "Doe", "Adaptive");

        // Update each field
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());

        user.setPassword("newPassword");
        assertTrue(user.validatePassword("newPassword"));

        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());

        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());

        user.setOrganisation("NewOrg");
        assertEquals("NewOrg", user.getOrganisation());

        user.setBlocked(true);
        assertTrue(user.isBlocked());
    }

    @Test
    @DisplayName("Users should not update with empty values")
    public void settersShouldNotUpdateWithEmptyValues()
    {
        final User user = new User("tammy1", "password", "Tammy", "Khan", "Adaptive");

        user.setUsername("");
        assertEquals("tammy1", user.getUsername());

        user.setPassword("");
        assertTrue(user.validatePassword("password"));
    }
}
