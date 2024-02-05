package com.weareadaptive.auction.model;

import com.weareadaptive.auction.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
                    final BusinessException exception = assertThrows(
                            BusinessException.class,
                            () -> new Auction(1, TestData.USER2, null, 100, 2.45));
                    assertTrue(exception.getMessage().contains("symbol"));
                },
                () ->
                {
                    final BusinessException exception = assertThrows(
                            BusinessException.class,
                            () -> new Auction(1, TestData.USER2, "", 100, 2.45));
                    assertTrue(exception.getMessage().contains("symbol"));
                }
        );
    }

    @Test
    @DisplayName("When the price is negative")
    public void priceIsNegative()
    {
        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> new Auction(1, TestData.USER2, TestData.USDJPY, -1, 2.45));

        assertTrue(exception.getMessage().contains("quantity"));
    }

    @Test
    @DisplayName("When the minimum price is negative")
    public void minPriceIsNegative()
    {
        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> new Auction(1, TestData.USER2, TestData.USDJPY, 100, -2.45));

        assertTrue(exception.getMessage().contains("price"));
    }

    @Test
    @DisplayName("Bid with same price as minPrice should throw exception")
    public void bidWithSamePriceAsMinPrice()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> auction.makeBid(10, 2.45, TestData.USER1));
        assertTrue(exception.getMessage().contains("price"));
    }

    @Test
    @DisplayName("When the price is less than the minPrice in a bid")
    public void bidPriceIsLessThanMinPrice()
    {
        final BusinessException exception = assertThrows(
                BusinessException.class,
                () ->
                {
                    final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100,
                            2.45);
                    auction.makeBid(100, 1.45, TestData.USER1);
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
        final BusinessException exception = assertThrows(
                BusinessException.class,
                () ->
                {
                    final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100,
                            2.45);
                    auction.makeBid(-1, 3, TestData.USER1);
                });
        assertTrue(exception.getMessage().contains("quantity"));
    }

    @Test
    @DisplayName("When making a bid on a closed auction")
    public void bidOnClosedAuction()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        auction.closeAuction();

        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> auction.makeBid(10, 3, TestData.USER1));
        assertTrue(exception.getMessage().contains("closed"));
    }

    @Test
    @DisplayName("Bid by auction owner should throw exception")
    public void bidByAuctionOwner()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        final BusinessException exception = assertThrows(
                BusinessException.class,
                () -> auction.makeBid(10, 3, TestData.USER2));
        assertTrue(exception.getMessage().contains("own auction"));
    }

    @Test
    @DisplayName("Should show closing summary")
    public void closingSummary()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);
        auction.makeBid(100, 3.00, TestData.USER1);

        auction.closeAuction();

        assertEquals(1, auction.getWinningBids(TestData.USER1).size());
        assertEquals(100, auction.getTotalSoldQuantity());

        final BigDecimal expectedRevenue = BigDecimal.valueOf(100 * 3.00);
        assertEquals(0, expectedRevenue.compareTo(auction.getTotalRevenue()));
    }

    @Test
    @DisplayName("Partial winning bids")
    public void partialWinningBids()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        auction.makeBid(60, 3.00, TestData.USER1);
        auction.makeBid(50, 2.50, TestData.USER3);

        auction.closeAuction();

        assertEquals(1, auction.getWinningBids(TestData.USER1).size());
        assertEquals(1, auction.getWinningBids(TestData.USER3).size());
        assertEquals(100, auction.getTotalSoldQuantity());

        final BigDecimal expectedRevenue = BigDecimal.valueOf(60 * 3.00 + 40 * 2.50);
        assertEquals(0, expectedRevenue.compareTo(auction.getTotalRevenue()));
    }

    @Test
    @DisplayName("Multiple bids with bid loss")
    public void multipleBidsWithBidLoss()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.USDJPY, 100, 2.45);

        auction.makeBid(50, 3.00, TestData.USER1);
        auction.makeBid(100, 2.50, TestData.USER3);
        auction.makeBid(50, 3.10, TestData.USER4);

        auction.closeAuction();

        assertEquals(1, auction.getWinningBids(TestData.USER4).size());
        assertEquals(1, auction.getWinningBids(TestData.USER1).size());
        assertEquals(1, auction.getLostBids(TestData.USER3).size());
        assertEquals(100, auction.getTotalSoldQuantity());

        final BigDecimal expectedRevenue = BigDecimal.valueOf(50 * 3.10 + 50 * 3.00);
        assertEquals(0, expectedRevenue.compareTo(auction.getTotalRevenue()));
    }

    @Test
    @DisplayName("Two identical bids to fill the order")
    public void twoIdenticalBidsToFillOrder()
    {
        final Auction auction = new Auction(1, TestData.USER2, TestData.EBAY, 50, 2.45);

        auction.makeBid(50, 3.00, TestData.USER1);
        auction.makeBid(50, 3.00, TestData.USER3);

        auction.closeAuction();

        assertEquals(1, auction.getWinningBids(TestData.USER1).size());
        assertEquals(1, auction.getLostBids(TestData.USER3).size());
    }
}
