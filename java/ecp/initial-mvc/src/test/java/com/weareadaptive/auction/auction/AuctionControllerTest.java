package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.IntegrationTest;
import com.weareadaptive.auction.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.HashMap;
import java.util.Map;

import static com.weareadaptive.auction.TestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuctionControllerTest extends IntegrationTest
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
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Override
    @BeforeEach
    public void initialiseRestAssuredMockMvcStandalone()
    {
        super.initialiseRestAssuredMockMvcStandalone(); // base URI is initialised with the port
        uri += "/api/auctions"; // Adjusts the base uri for this test class
    }

    @Test
    @DisplayName("Get all auctions")
    public void getAllAuctions()
    {
        //@formatter:off
        given()
                .baseUri(uri)
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .get("/")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(2));
        //@formatter:on
    }

    @Test
    @DisplayName("Create a new user")
    public void createUser()
    {
        final Map<String, String> requestBody = new HashMap<>();
        requestBody.put("userId", "1");
        requestBody.put("symbol", TestData.FB);
        requestBody.put("quantity", "10");
        requestBody.put("minPrice", "3.50");

        //@formatter:off
        given()
                .baseUri(uri)
                .contentType("application/json")
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .body(requestBody)
                .post("/")
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("symbol", equalTo(TestData.FB));
        //@formatter:on
    }

    @Test
    @DisplayName("Bid on an auction")
    public void bidOnAuction()
    {
        final Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("quantity", "10");
        requestBody.put("minPrice", "4.50");
        requestBody.put("userId", "1");

        //@formatter:off
        given()
                .baseUri(uri)
                .contentType("application/json")
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .body(requestBody)
                .post("/{auctionId}/bids", 2)
        .then()
                .statusCode(HttpStatus.OK.value());
        //@formatter:on
    }

    @Test
    @DisplayName("Close an auction")
    public void closeAuction()
    {
        //@formatter:off
        given()
                .baseUri(uri)
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .put("/{auctionId}/close", 1)
        .then()
                .statusCode(HttpStatus.OK.value());
        //@formatter:on
    }

    @Test
    @DisplayName("Remove an auction")
    public void removeAuction()
    {
        //@formatter:off
        given()
                .baseUri(uri)
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .put("/{auctionId}/remove", 1)
        .then()
                .statusCode(HttpStatus.OK.value());
        //@formatter:on
    }

    @Test
    @DisplayName("Get winning bids with invalid auction or user")
    public void shouldNotGetWinningBidsNonExistentAuctionUser()
    {
        int invalidAuctionId = -1;
        int invalidUserId = -1;

        //@formatter:off
        given()
                .baseUri(uri)
        .when()
                .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
                .get("/{auctionId}/{userId}/winningBids", invalidAuctionId, invalidUserId)
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        //@formatter:on
    }
}
