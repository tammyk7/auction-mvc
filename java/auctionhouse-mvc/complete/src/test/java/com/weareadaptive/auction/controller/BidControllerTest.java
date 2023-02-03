package com.weareadaptive.auction.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.weareadaptive.auction.IntegrationTest;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.service.AuctionLotService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BidControllerTest extends IntegrationTest {
  @Container
  public static PostgreSQLContainer<?> postgreSQL =
      new PostgreSQLContainer<>("postgres:13.2")
          .withUsername("testUsername")
          .withPassword("testPassword");
  @Autowired
  private AuctionLotService auctionLotService;
  @Autowired
  private TestData testData;
  @LocalServerPort
  private int port;
  private String uri;

  @DynamicPropertySource
  public static void postgreSqlProperties(@NotNull DynamicPropertyRegistry registry) {
    postgreSqlProperties(registry, postgreSQL);
  }

  @BeforeEach
  public void initialiseRestAssuredMockMvcStandalone() {
    uri = "http://localhost:" + port;
  }

  @DisplayName("get should return all bids for a user")
  @Test
  public void shouldReturnAllBids() {
    var user = testData.createRandomUser();
    var auction = testData.createAuctionLot(
        testData.user1(),
        TestData.Stock.META,
        100,
        30
    );
    auctionLotService.bid(user.getUsername(), auction.getId(), 25, 99);
    auctionLotService.bid(testData.user2().getUsername(), auction.getId(), 90, 120);
    //auction.close();

    var auction2 = testData.createAuctionLot(
        testData.user1(),
        TestData.Stock.APPLE,
        10,
        40
    );
    auctionLotService.bid(user.getUsername(), auction2.getId(), 43, 199);

    var token = testData.getToken(user);

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, token)
    .when()
      .get("/bids")
    .then()
        .log().body()
      .statusCode(HttpStatus.OK.value())
        .body("size()", equalTo(2))
        .body("[0].username", equalTo(user.getUsername()))
        .body("[0].quantity", equalTo(25))
        .body("[0].price", equalTo(99F))
        .body("[1].username", equalTo(user.getUsername()))
        .body("[1].quantity", equalTo(43))
        .body("[1].price", equalTo(199F))
        .body("[1].state", equalTo("PENDING"));
    //@formatter:on
  }
}
