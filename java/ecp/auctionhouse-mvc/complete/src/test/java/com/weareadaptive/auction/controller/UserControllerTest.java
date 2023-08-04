package com.weareadaptive.auction.controller;

import static com.weareadaptive.auction.TestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.github.javafaker.Faker;
import com.weareadaptive.auction.IntegrationTest;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.controller.dto.CreateUserRequest;
import com.weareadaptive.auction.controller.dto.UpdateUserRequest;
import com.weareadaptive.auction.service.UserService;
import io.restassured.http.ContentType;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends IntegrationTest {
  public static final int INVALID_USER_ID = 99999;
  @Container
  public static PostgreSQLContainer<?> postgreSQL =
      new PostgreSQLContainer<>("postgres:13.2")
          .withUsername("testUsername")
          .withPassword("testPassword");
  private final Faker faker = new Faker();
  @Autowired
  ApplicationContext applicationContext;
  @Autowired
  private UserService userService;

  @DynamicPropertySource
  public static void postgreSqlProperties(@NotNull DynamicPropertyRegistry registry) {
    postgreSqlProperties(registry, postgreSQL);
  }

  @DisplayName("create should return a bad request when the username is duplicated")
  @Test
  public void create_shouldReturnBadRequestIfUserExist() {
    var createRequest = new CreateUserRequest(
        testData.user1().getUsername(),
        "dasfasdf",
        testData.user1().getFirstName(),
        testData.user1().getFirstName(),
        testData.user1().getOrganisation());

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .contentType(ContentType.JSON)
      .body(createRequest)
    .when()
      .post("/users")
    .then()
      .statusCode(BAD_REQUEST.value())
      .body("message", containsString("already exist"));
    //@formatter:on
  }

  @DisplayName("getAll should return all users")
  @Test
  public void getAll_shouldReturnAllUsers() {
    var find1 = format("find { it.id == %s }.", testData.user1().getId());
    var find2 = format("find { it.id == %s }.", testData.user2().getId());

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
    .when()
      .get("/users")
    .then()
      .statusCode(HttpStatus.OK.value())
      // Validate User1
      .body(find1 + "username", equalTo(testData.user1().getUsername()))
      .body(find1 + "firstName", equalTo(testData.user1().getFirstName()))
      .body(find1 + "lastName", equalTo(testData.user1().getLastName()))
      .body(find1 + "organisation", equalTo(testData.user1().getOrganisation()))
      // Validate User2
      .body(find2 + "username", equalTo(testData.user2().getUsername()))
      .body(find2 + "firstName", equalTo(testData.user2().getFirstName()))
      .body(find2 + "lastName", equalTo(testData.user2().getLastName()))
      .body(find2 + "organisation", equalTo(testData.user2().getOrganisation()));
    //@formatter:on
  }

  @DisplayName("get should when return 404 when user doesn't")
  @Test
  public void shouldReturn404WhenUserDontExist() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .pathParam("id", INVALID_USER_ID)
    .when()
      .get("/users/{id}")
    .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("should be an admin to access User API")
  @Test
  public void shouldReturnForbiddenIfNotAnAdmin() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParam("id", INVALID_USER_ID)
    .when()
      .get("/users/{id}")
    .then()
      .statusCode(FORBIDDEN.value());
    //@formatter:on
  }

  @DisplayName("get should return a user")
  @Test
  public void shouldReturnUserIfExists() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .pathParam("id", testData.user1().getId())
    .when()
      .get("/users/{id}")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body("id", equalTo(testData.user1().getId()))
      .body("firstName", equalTo(testData.user1().getFirstName()))
      .body("lastName", equalTo(testData.user1().getLastName()))
      .body("organisation", equalTo(testData.user1().getOrganisation()));
    //@formatter:on
  }

  @DisplayName("update should return 404 when user is not found")
  @Test
  public void update_shouldReturnNotFoundWhenUpdatingNonExistingUser() {
    var name = faker.name();
    var updateRequest =
        new UpdateUserRequest(
            name.firstName(),
            name.lastName(),
            faker.company().name());

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .contentType(ContentType.JSON)
      .pathParam("id", INVALID_USER_ID)
      .body(updateRequest)
    .when()
      .put("/users/{id}")
    .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("update should update and return the user")
  @Test
  public void shouldUpdateUserIfExists() {
    var newUser = testData.createRandomUser();
    var name = faker.name();
    var updateRequest =
        new UpdateUserRequest(
            name.firstName(),
            name.lastName(),
            faker.company().name());

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .contentType(ContentType.JSON)
      .pathParam("id", newUser.getId())
      .body(updateRequest)
    .when()
      .put("/users/{id}")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body("id", equalTo(newUser.getId()))
      .body("firstName", equalTo(updateRequest.firstName()))
      .body("lastName", equalTo(updateRequest.lastName()))
      .body("organisation", equalTo(updateRequest.organisation()));
    //@formatter:on

    var user = userService.getUser(newUser.getId());
    assertThat(user.isPresent(), equalTo(true));
    assertThat(user.get().getFirstName(), equalTo(updateRequest.firstName()));
    assertThat(user.get().getLastName(), equalTo(updateRequest.lastName()));
    assertThat(user.get().getOrganisation(), equalTo(updateRequest.organisation()));
  }

  @DisplayName("block should block the user")
  @Test
  public void shouldBlockUser() {
    var user = testData.createRandomUser();

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .pathParam("id", user.getId())
    .when()
      .put("/users/{id}/block")
    .then()
      .statusCode(NO_CONTENT.value());
    //@formatter:on

    var blockedUser = userService.getUser(user.getId());
    assertThat(blockedUser.isPresent(), equalTo(true));
    assertThat(blockedUser.get().isBlocked(), equalTo(true));
  }

  @DisplayName("unblock should unblock the user")
  @Test
  public void shouldUnlockUser() {

    var user = testData.createRandomUser();
    userService.block(user.getId());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
        .pathParam("id", user.getId())
    .when()
        .put("/users/{id}/unblock")
    .then()
        .statusCode(NO_CONTENT.value());
    //@formatter:on

    var blockedUser = userService.getUser(user.getId());
    assertThat(blockedUser.isPresent(), equalTo(true));
    assertThat(user.isBlocked(), equalTo(false));
  }

  @DisplayName("unblock should return 404 when user is not found")
  @Test
  public void shouldUnblockUserReturnNotFound() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .pathParam("id", INVALID_USER_ID)
    .when()
      .put("/users/{id}/unblock")
    .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("block should return 404 user is not found")
  @Test
  public void shouldBlockUserReturnNotFound() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .pathParam("id", INVALID_USER_ID)
    .when()
      .put("/users/{id}/block")
    .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("create should create and return the new user")
  @Test
  public void shouldReturnUserIfCreated() {
    var name = faker.name();
    var createRequest =
        new CreateUserRequest(
            name.username(),
            TestData.PASSWORD,
            name.firstName(),
            name.lastName(),
            faker.company().name());

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .contentType(ContentType.JSON)
      .body(createRequest)
    .when()
      .post("/users")
    .then()
      .statusCode(HttpStatus.CREATED.value())
      .body("id", greaterThan(0))
      .body("firstName", equalTo(createRequest.firstName()))
      .body("lastName", equalTo(createRequest.lastName()))
      .body("organisation", equalTo(createRequest.organisation()));
    //@formatter:on
  }
}
