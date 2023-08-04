package com.weareadaptive.auction.controller;

import static com.weareadaptive.auction.TestData.ADMIN_AUTH_TOKEN;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.weareadaptive.auction.IntegrationTest;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.TestTimeContext;
import com.weareadaptive.auction.TimeContext;
import com.weareadaptive.auction.controller.dto.BidRequest;
import com.weareadaptive.auction.controller.dto.CreateAuctionLotRequest;
import com.weareadaptive.auction.service.AuctionLotService;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class AuctionLotControllerTest extends IntegrationTest {
  public static final int INVALID_AUCTION_LOT_ID = 33333;

  @Container
  public static PostgreSQLContainer<?> postgreSQL =
      new PostgreSQLContainer<>("postgres:13.2")
          .withUsername("testUsername")
          .withPassword("testPassword");
  @Autowired
  private AuctionLotService auctionLotService;

  @DynamicPropertySource
  public static void postgreSqlProperties(@NotNull DynamicPropertyRegistry registry) {
    postgreSqlProperties(registry, postgreSQL);
  }

  @DisplayName("get should return the auction")
  @Test
  public void shouldReturnAuctionWhenExist() {
    var auctionLot = testData.createAuctionLot(
        testData.user1(),
        TestData.Stock.APPLE,
        10000,
        2);

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParam("id", auctionLot.getId())
    .when()
      .get("/auction-lots/{id}")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body("id", equalTo(auctionLot.getId()))
      .body("symbol", equalTo(auctionLot.getSymbol()))
      .body("minPrice", equalTo((float) auctionLot.getMinPrice()))
      .body("quantity", equalTo(auctionLot.getQuantity()));
    //@formatter:on
  }

  @DisplayName("should return forbidden when the user is not a USER")
  @Test
  public void shouldReturnForbiddenIsNotAnUser() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, ADMIN_AUTH_TOKEN)
      .pathParam("id", INVALID_AUCTION_LOT_ID)
    .when()
      .get("/auction-lots/{id}")
    .then()
      .statusCode(FORBIDDEN.value());
  }

  @DisplayName("create should return and create the auction")
  @Test
  public void shouldCreateAuction() {
    var createAuction = new CreateAuctionLotRequest(
        "AAPL",
        5,
        100
    );

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .contentType(ContentType.JSON)
      .body(createAuction)
    .when()
        .post("/auction-lots")
    .then()
      .statusCode(HttpStatus.CREATED.value())
      .body("id", greaterThan(0))
      .body("owner", equalTo(testData.user1().getUsername()))
      .body("symbol", equalTo(createAuction.symbol()))
      .body("minPrice", equalTo((float)createAuction.minPrice()))
      .body("quantity",equalTo(createAuction.quantity()));
    //@formatter:on
  }

  @DisplayName("bid should create a new bid on the auction")
  @Test
  public void shouldBidToAnAuction() {
    var newAuction = testData.createAuctionLot(
        testData.user2(),
        TestData.Stock.META,
        10000, 23
    );
    var bidRequest = new BidRequest(
        25,
        100
    );

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParams("id", newAuction.getId())
      .contentType(ContentType.JSON)
      .body(bidRequest)
    .when()
      .post("/auction-lots/{id}/bid")
    .then()
      .statusCode(NO_CONTENT.value());
    //@formatter:on

    var bids = auctionLotService.getAuctionLotBids(newAuction.getId());
    assertThat(bids, hasSize(1));
    var bid = bids.get(0);
    assertThat(bid.getQuantity(), equalTo(bidRequest.quantity()));
    assertThat(bid.getPrice(), equalTo(bidRequest.price()));
  }

  @DisplayName("getAuctionBids should return all the bids of an auction")
  @Test
  public void shouldReturnAllAuctionBids() {
    var auctionLot = testData.createAuctionLot(
        testData.user1(),
        TestData.Stock.APPLE,
        10000,
        2);
    auctionLotService.bid(
        testData.user2().getUsername(),
        auctionLot.getId(),
        10,
        65);
    auctionLotService.bid(
        testData.user3().getUsername(),
        auctionLot.getId(),
        45,
        87);

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParams("id", auctionLot.getId())
    .when()
      .get("/auction-lots/{id}/bids")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body("size()", equalTo(2))
      .body("[0].price", equalTo(65F))
      .body("[0].quantity", equalTo(10))
      .body("[0].username", equalTo(testData.user2().getUsername()))
      .body("[1].price", equalTo(87F))
      .body("[1].quantity", equalTo(45))
      .body("[1].username", equalTo(testData.user3().getUsername()));
    //@formatter:on
  }

  @DisplayName("get Should return 404 when auction lot is not found")
  @Test
  public void shouldReturnNotFoundWhenGettingAuctionBidsFromAnUnexistingAuction() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParams("id", INVALID_AUCTION_LOT_ID)
    .when()
      .get("/auction-lots/{id}/bids")
    .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("getAuctionBids should return 404 when auction is not found")
  @Test
  public void shouldReturnNotFoundWhenBidOnNonExistingAuctionLot() {
    var bidRequest = new BidRequest(
        25,
        100
    );

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParams("id", INVALID_AUCTION_LOT_ID)
      .contentType(ContentType.JSON)
      .body(bidRequest)
    .when()
      .post("/auction-lot/{id}/bids")
    .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("closeAuction should return 404 when the auction is not found")
  @Test
  public void shouldReturnNotFoundWhenClosingAnInvalidAuction() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParams("id", INVALID_AUCTION_LOT_ID)
    .when()
      .post("/auction-lot/{id}/close")
    .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("closeAuction should return forbidden when the user is not the owner")
  @Test
  public void shouldReturnForbiddenWhenClosingAnAuctionYouAreNotTheOwner() {
    var newAuction = testData.createAuctionLot(
        testData.user2(),
        TestData.Stock.META,
        10000, 23
    );

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParams("id", newAuction.getId())
    .when()
      .post("/auction-lots/{id}/close")
    .then()
      .statusCode(FORBIDDEN.value());
    //@formatter:on
  }

  @DisplayName("closeAuction should close an auction and return it's closing summary")
  @Test
  public void shouldCloseAnAuction() {
    var newAuction = testData.createAuctionLot(
        testData.user1(),
        TestData.Stock.APPLE,
        10000,
        1
    );
    auctionLotService.bid(testData.user2().getUsername(), newAuction.getId(), 100, 500);

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParams("id", newAuction.getId())
      .contentType(ContentType.JSON)
    .when()
      .post("/auction-lots/{id}/close")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body("totalSoldQuantity", equalTo(100))
      .body("totalRevenue", equalTo(50000f))
      .body("winBids.size()", equalTo(1))
      .body("winBids[0].winQuantity", equalTo(100))
      .body("winBids[0].username", equalTo(testData.user2().getUsername()))
      .body("winBids[0].quantity", equalTo(100))
      .body("winBids[0].price", equalTo(500f));
    //@formatter:on

    var closingSummary =
        auctionLotService.getClosingSummary(testData.user1().getUsername(), newAuction.getId());
    assertThat(closingSummary.totalSoldQuantity(), equalTo(100));
    assertThat(closingSummary.totalRevenue(), equalTo(BigDecimal.valueOf(50000.0)));
    assertThat(closingSummary.winBids().size(), equalTo(1));
    assertThat(closingSummary.winBids().get(0).getWinQuantity(), equalTo(100));
    assertThat(closingSummary.winBids().get(0).getUsername(),
        equalTo(testData.user2().getUsername()));
    assertThat(closingSummary.winBids().get(0).getQuantity(), equalTo(100));
    assertThat(closingSummary.winBids().get(0).getPrice(), equalTo(500.0));
  }

  @DisplayName("getAll should return all auctions")
  @Test
  public void shouldReturnAllAuctions() {
    var auctionLot1 = testData.createAuctionLot(
        testData.user1(),
        TestData.Stock.APPLE,
        10000,
        1
    );
    var auctionLot2 = testData.createAuctionLot(
        testData.user2(),
        TestData.Stock.META,
        543,
        144
    );
    var find1 = format("find { it.id == %s }.", auctionLot1.getId());
    var find2 = format("find { it.id == %s }.", auctionLot2.getId());

    //@formatter:off
    given()
        .baseUri(uri)
        .header(AUTHORIZATION, testData.user1Token())
    .when()
      .get("/auction-lots")
    .then()
      .statusCode(HttpStatus.OK.value())
      //Validate Auction1
      .body(find1 + "symbol", equalTo(auctionLot1.getSymbol()))
      .body(find1 + "minPrice", equalTo((float)auctionLot1.getMinPrice()))
      .body(find1 + "quantity",equalTo(auctionLot1.getQuantity()))
      //Validate Auction2
      .body(find2 + "symbol", equalTo(auctionLot2.getSymbol()))
      .body(find2 + "minPrice", equalTo((float)auctionLot2.getMinPrice()))
      .body(find2 + "quantity",equalTo(auctionLot2.getQuantity()));
    //@formatter:on
  }

  @DisplayName("get should return not found when auction is not found")
  @Test
  public void shouldReturnNotFoundWhenAuctionDoesntExist() {
    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user1Token())
      .pathParam("id", INVALID_AUCTION_LOT_ID)
    .when()
      .get("/auction-lots/{id}")
    .then()
      .statusCode(NOT_FOUND.value());
    //@formatter:on
  }

  @DisplayName("get should return all the closing summary when auction is closed")
  @Test
  @Ignore
  public void shouldReturnAuctionClosingSummary() {

    var auctionLot = testData.createAuctionLot(
        testData.user2(),
        TestData.Stock.APPLE,
        200,
        21);
    TestTimeContext.useFixedTime();
    auctionLotService.bid(testData.user1().getUsername(), auctionLot.getId(), 1000, 120);
    auctionLotService.bid(testData.user3().getUsername(), auctionLot.getId(), 120, 200);
    auctionLotService.bid(testData.user4().getUsername(), auctionLot.getId(), 100, 300);
    auctionLotService.close(auctionLot.getOwner(), auctionLot.getId());

    var now = TimeContext.timeProvider().now();

    //@formatter:off
    given()
      .baseUri(uri)
      .header(AUTHORIZATION, testData.user2Token())
      .pathParam("id", auctionLot.getId())
    .when()
      .get("/auction-lots/{id}/closingSummary")
    .then()
      .statusCode(HttpStatus.OK.value())
      .body("winBids[0].username", equalTo(testData.user3().getUsername()))
      .body("winBids[0].quantity", equalTo(120))
      .body("winBids[0].price", equalTo(200.0F))
      .body("winBids[1].quantity", equalTo(100))
      .body("winBids[1].username", equalTo(testData.user4().getUsername()))
      .body("winBids[1].price", equalTo(300.0F))
      .body("totalSoldQuantity", equalTo(200))
      .body("totalRevenue", equalTo(50000.0F))
      .body("closedAt", equalTo(now.toEpochMilli()));
    //@formatter:on
  }
}
