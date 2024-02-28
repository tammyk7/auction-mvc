package com.weareadaptive.auction.security;

import com.weareadaptive.auction.IntegrationTest;
import com.weareadaptive.auction.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static com.weareadaptive.auction.TestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class SecurityTest extends IntegrationTest
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

    @Override
    @BeforeEach
    public void initialiseRestAssuredMockMvcStandalone()
    {
        super.initialiseRestAssuredMockMvcStandalone();
        uri += "/api";
    }

    @Test
    public void shouldBeUnauthorizedWhenNotAuthenticated()
    {
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
    public void shouldBeAuthenticated()
    {
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
    public void shouldBeAnAdmin()
    {
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

    //TODO
    @Disabled
    @Test
    public void shouldReturnForbiddenWhenNotAnAdmin()
    {
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
