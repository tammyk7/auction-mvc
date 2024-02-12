package com.weareadaptive.auction.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.weareadaptive.auction.TestData;


import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuctionCollectionTest
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
        auction.makeBid(new Bid(1, 100, 3.00, Instant.now(), TestData.USER1));
        auction.makeBid(new Bid(1, 60, 2.50, Instant.now(), TestData.USER4));

        // Close the auction
        auction.close();

        // Add the auction to the state
        auctionCollection.addAuction(auction);

        // Find lost bids for TestData.USER4
        final List<Bid> lostBids = auctionCollection.getAuction(1).getLostBids();

        assertEquals(1, lostBids.size());
        final Bid bidLost = lostBids.get(0);
        assertEquals(TestData.USER4, bidLost.getUser());
        assertEquals(60, bidLost.getQuantity());
        assertEquals(2.50, bidLost.getPrice());
    }

    @Test
    @DisplayName("Find won bids for a user")
    public void findWonBidsForUser()
    {
        // Create an auction and add bids
        final Auction auction = new Auction(1, TestData.USER2, TestData.FB, 100, 2.45);
        auction.makeBid(new Bid(1, 100, 3.00, Instant.now(), TestData.USER1));
        auction.makeBid(new Bid(1, 60, 2.50, Instant.now(), TestData.USER4));

        // Close the auction
        auction.close();

        // Add the auction to the state
        auctionCollection.addAuction(auction);

        // Find won bids for TestData.USER1
        final List<Bid> wonBids = auctionCollection.getAuction(1).getWinningBids();

        // Verify that TestData.USER1 has won bids
        assertEquals(1, wonBids.size());
        final Bid bidWon = wonBids.get(0);
        assertEquals(TestData.USER1, bidWon.getUser());
        assertEquals(100, bidWon.getQuantity());
        assertEquals(3.00, bidWon.getPrice());
    }
}
