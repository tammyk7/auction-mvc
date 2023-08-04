package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.TestData.USER1;
import static com.weareadaptive.auctionhouse.TestData.USER2;
import static com.weareadaptive.auctionhouse.TestData.USER3;
import static com.weareadaptive.auctionhouse.TestData.USER4;
import static com.weareadaptive.auctionhouse.model.AuctionLot.Status.CLOSED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.weareadaptive.auctionhouse.TestTimeContext;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class AuctionLotTest {

  @Test
  @DisplayName("Should throw when the owner is null")
  public void shouldThrowWhenOwnerIsNull() {
    var exception = assertThrows(
        BusinessException.class,
        () -> new AuctionLot(1, null, "AAPL", 3, 5));
    assertTrue(exception.getMessage().contains("owner"));
  }

  @Test
  @DisplayName("Should throw when title is null or empty")
  public void shouldThrowWhenTheTitleIsEmpty() {
    var exception = assertThrows(
        BusinessException.class,
        () -> new AuctionLot(1, USER1, null, 3, 5));
    assertTrue(exception.getMessage().contains("symbol"));
  }

  @Test
  @DisplayName("Should throw when the quantity is not above 0")
  public void shouldThrowWhenTheQuantityIsNotAboveZero() {
    var exception = assertThrows(
        BusinessException.class,
        () -> new AuctionLot(1, USER1, "AAPL", -1, 5));
    assertTrue(exception.getMessage().contains("quantity"));
  }

  @Test
  @DisplayName("Should throw when the minPrice is not above 0")
  public void shouldThrowWhenTheMinPriceIsNotAboveZero() {
    var exception = assertThrows(
        BusinessException.class,
        () -> new AuctionLot(1, USER1, "AAPL", 89, -5));
    assertTrue(exception.getMessage().contains("minPrice"));
  }

  @Test
  @DisplayName("Should throw when bidder is the owner")
  public void shouldThrowWhenTheBidderIsTheOwner() {
    //Arrange
    var lot = new AuctionLot(1, USER1, "AAPL", 89, 5);
    //TODO: Use a constant
    //Act
    var exception = assertThrows(
        BusinessException.class,
        () -> lot.bid(USER1, 1, 8), "Owner cannot bid on his account lot");
  }

  @Test
  @DisplayName("Should throw when bidding with a quantity bellow 1")
  public void shouldThrowWhenBiddingWithZeroOrNegativeQuantity() {
    //Arrange
    var lot = new AuctionLot(1, USER1, "AAPL", 89, 5);

    //Act
    var exception = assertThrows(
        BusinessException.class,
        () -> lot.bid(USER2, -1, 8));

    //Assert
    assertTrue(exception.getMessage().contains("quantity"));
  }

  @Test
  @DisplayName("Should throw when bidding with a price is bellow the minimum price")
  public void shouldThrowWhenTheBiderIsTheOwner() {
    var exception = assertThrows(
        BusinessException.class,
        () -> {
          var lot = new AuctionLot(1, USER1, "AAPL", 89, 5);
          lot.bid(USER2, 3, 3);
        });
    assertTrue(exception.getMessage().contains("price"));
  }

  @Test
  @DisplayName("Should not be able to bid when the auction is closed")
  public void shouldNotBeAbleToBidWhenAuctionIsClosed() {
    //Arrange
    var lot = new AuctionLot(1, USER1, "AAPL", 89, 5);
    lot.close();

    //Act
    var exception = assertThrows(
        BusinessException.class,
        () -> lot.bid(USER2, 34, 12));

    //Assert
    assertTrue(exception.getMessage().contains("closed"));
  }

  @Test
  @DisplayName("Should be able to add a bid")
  public void shouldBeAbleToBid() {
    //Arrange
    var lot = new AuctionLot(1, USER1, "AAPL", 89, 5);

    //Act
    lot.bid(USER2, 23, 32);

    //Assert
    var bids = lot.getBids();
    assertEquals(1, bids.size());
    var bid = bids.get(0);
    assertEquals(USER2, bid.user());
    assertEquals(23, bid.quantity());
    assertEquals(32, bid.price());
  }

  @Test
  @DisplayName("Should not be able to close an already closed auction lot")
  public void shouldNotBeAbleToClosedWhenAuctionIsClosed() {
    //Arrange
    var lot = new AuctionLot(1, USER1, "AAPL", 89, 5);
    lot.close();

    //Act
    var exception = assertThrows(
        BusinessException.class,
        lot::close);

    //Assert
    assertTrue(exception.getMessage().contains("closed"));
  }

  @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
  @Test
  @DisplayName("Should be able to add a bid")
  public void shouldCalculateTheWinnersOnClose() {
    //Arrange
    var lot = new AuctionLot(1, USER1, "AAPL", 50, 5);

    TestTimeContext.userNormal();
    lot.bid(USER4, 30, 10);
    lot.bid(USER3, 40, 25);
    lot.bid(USER2, 20, 32);
    lot.bid(USER4, 40, 25);

    final Instant now = TestTimeContext.useFixedTime();

    //Act
    lot.close();

    //Assert
    assertEquals(CLOSED, lot.getStatus());
    var summary = lot.getClosingSummary();
    assertEquals(50, summary.totalSoldQuantity());
    assertEquals(BigDecimal.valueOf(1390.0), summary.totalRevenue());
    assertEquals(now, summary.closingTime());

    var winners = summary.winningBids();
    assertEquals(2, winners.size());

    var firstWinner = winners.get(0);
    assertEquals(USER2, firstWinner.originalBid().user());
    assertEquals(20, firstWinner.quantity());

    var secondWinner = winners.get(1);
    assertEquals(USER3, secondWinner.originalBid().user());
    assertEquals(30, secondWinner.quantity());
  }

  @Test
  @DisplayName("Should throw when accessing the closing summary for an not closed auction")
  public void shouldThrowWhenAccessingClosingSummaryThatAuctionIsNotClosed() {
    //Arrange
    var lot = new AuctionLot(1, USER1, "AAPL", 50, 5);

    //Act
    var exception = assertThrows(BusinessException.class, lot::getClosingSummary);

    //Arrange
    assertTrue(exception.getMessage().contains("closed"));
  }

}
