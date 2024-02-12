package com.weareadaptive.auction.model;

import com.weareadaptive.auction.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BidTest
{
    private BidCollection bidCollection;

    @BeforeEach
    public void beforeEach()
    {
        bidCollection = new BidCollection();

        bidCollection.add(new Bid(
                1,
                100,
                10.0,
                Instant.now(),
                TestData.USER1)
        );
        bidCollection.add(new Bid(
                2,
                200,
                20.0,
                Instant.now(),
                TestData.USER1)
        );
        bidCollection.add(new Bid(
                3,
                300,
                30.0,
                Instant.now(),
                TestData.USER2)
        );
    }

    @Test
    @DisplayName("When the timestamp is null")
    public void timestampIsNull()
    {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> new Bid(1, 100, 2.45, null, TestData.USER1));

        assertTrue(exception.getMessage().contains("timestamp"));
    }

    @Test
    @DisplayName("When the quantity is 0")
    public void quantityIsZero()
    {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> new Bid(1, 0, 2.45, Instant.now(), TestData.USER1));

        assertTrue(exception.getMessage().contains("quantity"));
    }

    @Test
    @DisplayName("Retrieve user-specific bids")
    public void getUserBids()
    {
        final List<Bid> user1Bids = bidCollection.getUserBids(TestData.USER1.getId());

        assertEquals(2, user1Bids.size());
        assertTrue(user1Bids.stream().allMatch(bid -> bid.getUser().equals(TestData.USER1)));
    }
}
