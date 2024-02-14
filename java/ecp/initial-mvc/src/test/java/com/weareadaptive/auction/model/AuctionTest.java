package com.weareadaptive.auction.model;

import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.auction.Auction;
import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionTest
{

    @Test
    @DisplayName("Receive the correct information from the Auction")
    public void testAuctionGetters()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        assertEquals(TestData.USER2, auction.getUser());
        assertEquals(TestData.USDJPY, auction.getSymbol());
        assertEquals(100, auction.getQuantity());
        assertEquals(2.45, auction.getMinPrice());
        assertEquals(Auction.AuctionStatus.OPEN, auction.getStatus());
    }

    @Test
    @DisplayName("Should throw when the symbol is empty or null in an auction")
    public void symbolIsNullOrEmpty()
    {
        assertAll("Inserting the symbol incorrectly in the auction",
                () ->
                {
                    final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                            AuthenticationExceptionHandling.BusinessException.class,
                            () -> new Auction(1, TestData.USER2, null, 100, 2.45));
                    assertTrue(exception.getMessage().contains("symbol"));
                },
                () ->
                {
                    final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                            AuthenticationExceptionHandling.BusinessException.class,
                            () -> new Auction(1, TestData.USER2, "", 100, 2.45));
                    assertTrue(exception.getMessage().contains("symbol"));
                }
        );
    }

    @Test
    @DisplayName("When the price is negative")
    public void priceIsNegative()
    {
        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> new Auction(1, TestData.USER2, TestData.USDJPY, -1, 2.45));

        assertTrue(exception.getMessage().contains("quantity"));
    }

    @Test
    @DisplayName("When the minimum price is negative")
    public void minPriceIsNegative()
    {
        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> new Auction(1, TestData.USER2, TestData.USDJPY, 100, -2.45));

        assertTrue(exception.getMessage().contains("price"));
    }

    @Test
    @DisplayName("Bid with same price as minPrice should throw exception")
    public void bidWithSamePriceAsMinPrice()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auction.makeBid(new Bid(1, 10, 2.45, Instant.now(), TestData.USER1)));
        assertTrue(exception.getMessage().contains("price"));
    }

    @Test
    @DisplayName("When the price is less than the minPrice in a bid")
    public void bidPriceIsLessThanMinPrice()
    {
        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () ->
                {
                    final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100,
                            2.45);
                    auction.makeBid(new Bid(1, 100, 1.45, Instant.now(), TestData.USER1));
                });
        assertTrue(exception.getMessage().contains("price"));
    }

    @Test
    @DisplayName("getMinPrice should return the correct minimum price of the auction")
    public void getMinPriceTest()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        assertEquals(2.45, auction.getMinPrice());
    }

    @Test
    @DisplayName("When the quantity is negative in a bid")
    public void bidQuantityIsNegative()
    {
        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () ->
                {
                    final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100,
                            2.45);
                    auction.makeBid(new Bid(1, -1, 3, Instant.now(), TestData.USER1));
                });
        assertTrue(exception.getMessage().contains("quantity"));
    }

    @Test
    @DisplayName("When making a bid on a closed auction")
    public void bidOnClosedAuction()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        auction.close();

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auction.makeBid(new Bid(1, 10, 3, Instant.now(), TestData.USER1)));
        assertTrue(exception.getMessage().contains("closed"));
    }

    @Test
    @DisplayName("Bid by auction owner should throw exception")
    public void bidByAuctionOwner()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auction.makeBid(new Bid(1, 10, 3, Instant.now(), TestData.USER2)));
        assertTrue(exception.getMessage().contains("own auction"));
    }

    @Test
    @DisplayName("Should show closing summary")
    public void closingSummary()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);
        auction.makeBid(new Bid(1, 100, 3.00, Instant.now(), TestData.USER1));

        auction.close();

        assertEquals(1, auction.getWinningBids().size());
        assertEquals(100, auction.getTotalSoldQuantity());

        final BigDecimal expectedRevenue = BigDecimal.valueOf(100 * 3.00);
        assertEquals(0, expectedRevenue.compareTo(auction.getTotalRevenue()));
    }

    @Test
    @DisplayName("Partial winning bids")
    public void partialWinningBids()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        auction.makeBid(new Bid(1, 60, 3.00, Instant.now(), TestData.USER1));
        auction.makeBid(new Bid(1, 50, 2.50, Instant.now(), TestData.USER3));

        auction.close();

        assertEquals(2, auction.getWinningBids().size());
        assertEquals(100, auction.getTotalSoldQuantity());

        final BigDecimal expectedRevenue = BigDecimal.valueOf(60 * 3.00 + 40 * 2.50);
        assertEquals(0, expectedRevenue.compareTo(auction.getTotalRevenue()));
    }

    @Test
    @DisplayName("Multiple bids with bid loss")
    public void multipleBidsWithBidLoss()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        auction.makeBid(new Bid(1, 50, 3.00, Instant.now(), TestData.USER1));
        auction.makeBid(new Bid(2, 100, 2.50, Instant.now(), TestData.USER3));
        auction.makeBid(new Bid(3, 50, 3.10, Instant.now(), TestData.USER4));

        auction.close();

        assertEquals(2, auction.getWinningBids().size());
        assertEquals(1, auction.getLostBids().size());
        assertEquals(100, auction.getTotalSoldQuantity());

        final BigDecimal expectedRevenue = BigDecimal.valueOf(50 * 3.10 + 50 * 3.00);
        assertEquals(0, expectedRevenue.compareTo(auction.getTotalRevenue()));
    }

    @Test
    @DisplayName("Two identical bids to fill the order")
    public void twoIdenticalBidsToFillOrder()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.EBAY, 50, 2.45);

        auction.makeBid(new Bid(1, 50, 3.00, Instant.now(), TestData.USER1));
        auction.makeBid(new Bid(2, 50, 3.00, Instant.now(), TestData.USER3));

        auction.close();

        assertEquals(1, auction.getWinningBids().size());
        assertEquals(1, auction.getLostBids().size());
    }
}
