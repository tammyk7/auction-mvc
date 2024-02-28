package com.weareadaptive.auction.user;

import com.weareadaptive.auction.IntegrationTest;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;

import static com.weareadaptive.auction.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest extends IntegrationTest
{

    // Initialise the PostgreSQL container
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("integration-tests-db")
            .withUsername("testUsername")
            .withPassword("testPassword");

    // Dynamically replace the data source properties with those of the running container
    @DynamicPropertySource
    public static void postgresqlProperties(DynamicPropertyRegistry registry)
    {
        postgreSqlProperties(registry, postgreSQLContainer);
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find user by username")
    public void shouldFindUserByUsername()
    {
        userRepository.save(USER1);
        final Optional<User> foundUser = userRepository.findByUsername(USER1.getUsername());

        assertTrue(foundUser.isPresent());
        assertEquals(USER1.getUsername(), foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty optional for non existent user")
    public void findByNonExistingUsername()
    {
        final Optional<User> foundUser = userRepository.findByUsername("nonexistentUser");
        assertTrue(foundUser.isEmpty());
    }
}
