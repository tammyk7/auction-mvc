package com.weareadaptive.auction.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.weareadaptive.auction.TestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest
{
    @LocalServerPort
    int port;
    String uri;

    @BeforeEach
    public void beforeEach()
    {
        uri = "http://localhost:" + port + "/api/users";
    }

    @Test
    @DisplayName("Get all users")
    public void getAllUsers()
    {
        //@formatter:off
        given()
                .baseUri(uri)
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .get("/")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(3));
        //@formatter:on
    }

    @Test
    @DisplayName("Create a new user")
    public void createUser()
    {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", "newUser");
        requestBody.put("password", "password");
        requestBody.put("firstname", "First");
        requestBody.put("lastname", "Last");
        requestBody.put("organisation", "Organisation");

        //@formatter:off
        given()
                .baseUri(uri)
                .contentType("application/json")
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .body(requestBody)
                .post("/")
        .then()
                .statusCode(HttpStatus.OK.value());
        //@formatter:on
    }

    @Test
    @DisplayName("Get user by userId")
    public void getUserById()
    {
        //@formatter:off
        given()
                .baseUri(uri)
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .get("/1")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", notNullValue());
        //@formatter:on
    }

    @Test
    @DisplayName("Should throw an exception when grabbing user with a negative id")
    public void shouldThrowBusinessExceptionWhenGrabbingNonExistentUser()
    {
        //@formatter:off
        given()
                .baseUri(uri)
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .get("/-1")
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        //@formatter:on
    }

    @Test
    @DisplayName("Update user status")
    public void updateUserStatus()
    {
        final Map<String, Boolean> requestBody = new HashMap<>();
        requestBody.put("isBlocked", true);

        //@formatter:off
        given()
                .baseUri(uri)
                .contentType("application/json")
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .body(requestBody)
                .put("/{userId}/status", 1)
        .then()
                .statusCode(HttpStatus.OK.value());
        //@formatter:on
    }

    @Test
    @DisplayName("Should throw an exception when updating a non existent user")
    public void shouldThrowBusinessExceptionWhenUpdatingNonExistentUser()
    {
        final Map<String, Boolean> requestBody = new HashMap<>();
        requestBody.put("isBlocked", true);

        //@formatter:off
        given()
                .baseUri(uri)
                .contentType("application/json")
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .body(requestBody)
                .put("/{userId}/status", -1)
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        //@formatter:on
    }
}
