package com.weareadaptive.auction.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.weareadaptive.auction.TestData;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuctionStateTest
{
    private AuctionCollection auctionCollection;

    @BeforeEach
    public void setUp()
    {
        auctionCollection = new AuctionCollection();
    }

    @Test
    @DisplayName("Find lost bids for a user")
    public void findLostBidsForUser()
    {
        // Create an auction and add bids
        final Auction auction = new Auction(1, TestData.USER2, TestData.AAPL, 100, 2.45);
        auction.makeBid(100, 3.00, TestData.USER1);
        auction.makeBid(60, 2.50, TestData.USER4);

        // Close the auction
        auction.closeAuction();

        // Add the auction to the state
        auctionCollection.addAuction(auction);

        // Find lost bids for TestData.USER4
        final List<BidLost> lostBids = auctionCollection.findLostBids(TestData.USER4);

        assertEquals(1, lostBids.size());
        BidLost bidLost = lostBids.get(0);
        assertEquals(1, bidLost.auctionId());
        assertEquals(TestData.AAPL, bidLost.symbol());
        assertEquals(60, bidLost.quantity());
        assertEquals(2.50, bidLost.price());
    }

    @Test
    @DisplayName("Find won bids for a user")
    public void findWonBidsForUser()
    {
        // Create an auction and add bids
        final Auction auction = new Auction(1, TestData.USER2, TestData.AAPL, 100, 2.45);
        auction.makeBid(100, 3.00, TestData.USER1);
        auction.makeBid(60, 2.50, TestData.USER4);

        // Close the auction
        auction.closeAuction();

        // Add the auction to the state
        auctionCollection.addAuction(auction);

        // Find won bids for TestData.USER1
        final List<BidWon> wonBids = auctionCollection.findWonBids(TestData.USER1);

        // Verify that TestData.USER1 has won bids
        assertEquals(1, wonBids.size());
        final BidWon bidWon = wonBids.get(0);
        assertEquals(1, bidWon.auctionId());
        assertEquals(TestData.AAPL, bidWon.symbol());
        assertEquals(100, bidWon.wonQuantity());
        assertEquals(100, bidWon.bidQuantity());
        assertEquals(3.00, bidWon.price());
    }
}
