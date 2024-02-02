package com.weareadaptive.auction.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest
{
    @LocalServerPort
    int port;
    String uri;

    @BeforeEach
    public void beforeEach()
    {
        uri = "http://localhost:" + port + "/users";
    }

    @Test
    @DisplayName("Get all users")
    public void getAllUsers()
    {
        given().baseUri(uri)
                .when()
                .get("/")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("users", hasSize(2));
    }

    @Test
    @DisplayName("Get user by userId")
    public void getUserById()
    {
        given().baseUri(uri)
                .when()
                .get("/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", notNullValue());
    }

    @Test
    @DisplayName("Update user status")
    public void updateUserStatus()
    {
        //Prepare request body
        final Map<String, Boolean> requestBody = new HashMap<>();
        requestBody.put("isBlocked", true);

        given()
                .baseUri(uri)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/{userId}/status", 1)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Update successful"));
    }

}
