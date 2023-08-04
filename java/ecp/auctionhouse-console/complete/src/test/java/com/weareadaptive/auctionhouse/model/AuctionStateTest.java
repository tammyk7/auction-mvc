package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.TestData.AAPL;
import static com.weareadaptive.auctionhouse.TestData.EBAY;
import static com.weareadaptive.auctionhouse.TestData.FB;
import static com.weareadaptive.auctionhouse.TestData.USER1;
import static com.weareadaptive.auctionhouse.TestData.USER2;
import static com.weareadaptive.auctionhouse.TestData.USER3;
import static com.weareadaptive.auctionhouse.TestData.USER4;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuctionStateTest {
  private AuctionState state;

  @BeforeEach
  public void initState() {
    state = new AuctionState();

    var lot = new AuctionLot(state.nextId(), USER1, AAPL, 500, 5);
    state.add(lot);
    lot.bid(USER2, 30, 10);
    lot.bid(USER3, 40, 25);
    lot.bid(USER4, 20, 32);
    lot.close();

    lot = new AuctionLot(state.nextId(), USER4, "MSFT", 500, 5);
    state.add(lot);
    lot.bid(USER3, 30, 10);
    lot.bid(USER2, 40, 25);
    lot.bid(USER1, 20, 32);
    lot.close();

    lot = new AuctionLot(state.nextId(), USER4, EBAY, 10, 5);
    state.add(lot);
    lot.bid(USER3, 30, 10);
    lot.bid(USER2, 40, 6);
    lot.close();

    lot = new AuctionLot(state.nextId(), USER4, FB, 10, 5);
    state.add(lot);
    lot.bid(USER3, 30, 10);
    lot.bid(USER2, 40, 8);
    lot.close();

    lot = new AuctionLot(state.nextId(), USER4, "AMZN", 500, 5);
    state.add(lot);
    lot.bid(USER3, 30, 10);
    lot.bid(USER2, 40, 25);
    lot.bid(USER1, 20, 32);
  }

  @Test
  @DisplayName("findWonBids should return the bids won by the user")
  public void shouldReturnWonBids() {
    //Arrange

    //Act
    var result = state.findWonBids(USER2);

    //Assert
    assertEquals(2, result.size());
    var firstBid = result.get(0);
    assertEquals(0, firstBid.auctionLotId());
    assertEquals(AAPL, firstBid.symbol());
    assertEquals(30, firstBid.wonQuantity());
    assertEquals(10.0, firstBid.price());

    var secondBid = result.get(1);
    assertEquals(1, secondBid.auctionLotId());
    assertEquals("MSFT", secondBid.symbol());
    assertEquals(40, secondBid.wonQuantity());
    assertEquals(25.0, secondBid.price());
    assertEquals(40, secondBid.bidQuantity());
  }

  @Test
  @DisplayName("findsLostBid should return the bids lost by the user")
  public void shouldReturnLostBids() {
    //Arrange

    //Act
    var result = state.findLostBids(USER2);

    //Assert
    assertEquals(2, result.size());
    var firstBid = result.get(0);
    assertEquals(2, firstBid.auctionLotId());
    assertEquals(EBAY, firstBid.symbol());
    assertEquals(40, firstBid.quantity());
    assertEquals(6.0, firstBid.price());

    var secondBid = result.get(1);
    assertEquals(3, secondBid.auctionLotId());
    assertEquals(FB, secondBid.symbol());
    assertEquals(40, secondBid.quantity());
    assertEquals(8.0, secondBid.price());
  }

}
