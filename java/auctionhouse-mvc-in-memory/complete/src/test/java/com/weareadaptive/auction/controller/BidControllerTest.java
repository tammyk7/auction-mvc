package com.weareadaptive.auction.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.weareadaptive.auction.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled("Need security enabled so Principal is available")
public class BidControllerTest {
  @Autowired
  private TestData testData;
  @LocalServerPort
  private int port;
  private String uri;

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
    auction.bid(user, 25, 99);
    auction.bid(testData.user2(), 90, 120);
    auction.close();

    var auction2 = testData.createAuctionLot(
        testData.user1(),
        TestData.Stock.APPLE,
        10,
        40
    );
    auction2.bid(user, 43, 199);

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
        .body("[0].state", equalTo("WIN"))
        .body("[0].winQuantity", equalTo(10))
        .body("[1].username", equalTo(user.getUsername()))
        .body("[1].quantity", equalTo(43))
        .body("[1].price", equalTo(199F))
        .body("[1].state", equalTo("PENDING"));
    //@formatter:on
  }
}
