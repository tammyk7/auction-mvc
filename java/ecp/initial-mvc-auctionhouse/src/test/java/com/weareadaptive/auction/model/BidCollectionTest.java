package com.weareadaptive.auction.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BidCollectionTest
{
    @Test
    @DisplayName("Get user bids")
    public void testGetUserBids()
    {
        final User user1 = new User(1,
                "user1",
                "password",
                "John",
                "Doe",
                "Company"
        );
        final User user2 = new User(
                2,
                "user2",
                "password",
                "Jane",
                "Doe",
                "Company"
        );

        final Bid bid1 = new Bid(
                1,
                10,
                5.0,
                Instant.now(),
                user1
        );
        final Bid bid2 = new Bid(
                2,
                15,
                7.0,
                Instant.now(),
                user2
        );
        final Bid bid3 = new Bid(
                3,
                20,
                8.0,
                Instant.now(),
                user1
        );

        final BidCollection bidCollection = new BidCollection();
        bidCollection.add(bid1);
        bidCollection.add(bid2);
        bidCollection.add(bid3);

        final List<Bid> user1Bids = bidCollection.getUserBids(1);

        assertEquals(2, user1Bids.size());
        assertEquals(bid1, user1Bids.get(0));
        assertEquals(bid3, user1Bids.get(1));
    }
}
