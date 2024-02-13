package com.weareadaptive.auction.security;

import static com.weareadaptive.auction.TestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.weareadaptive.auction.IntegrationTest;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class SecurityTest extends IntegrationTest {
  @Container
  public static PostgreSQLContainer<?> postgreSQL =
      new PostgreSQLContainer<>("postgres:13.2")
          .withUsername("testUsername")
          .withPassword("testPassword");
  @Autowired
  private TestData testData;
  @Autowired
  private UserService userService;

  @DynamicPropertySource
  public static void postgreSqlProperties(@NotNull DynamicPropertyRegistry registry) {
    postgreSqlProperties(registry, postgreSQL);
  }

  @Test
  @Disabled("Needs security enabled to work")
  public void shouldBeUnauthorizedWhenNotAuthenticated() {
    //@formatter:off
    given()
      .baseUri(uri)
    .when()
      .get("/test")
    .then()
      .statusCode(HttpStatus.UNAUTHORIZED.value());
    //@formatter:on
  }

  @Test
  public void shouldBeAuthenticated() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
    .when()
      .get("/test")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body(equalTo("houra"));
    //@formatter:on
  }

  @Test
  public void shouldBeAnAdmin() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
    .when()
      .get("/test/adminOnly")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body(equalTo("super"));
    //@formatter:on
  }

  @Test
  @Disabled("Needs security enabled to work")
  public void shouldReceiveForbiddenWhenNotAnAdmin() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
    .when()
      .get("/test/adminOnly")
    .then()
      .statusCode(HttpStatus.FORBIDDEN.value());
    //@formatter:on
  }
}
