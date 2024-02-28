package com.weareadaptive.auction.bid;

import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.bid.BidRepository;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BidTest
{
    private BidRepository bidRepository;

    @Test
    @DisplayName("When the timestamp is null")
    public void timestampIsNull()
    {
        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> new Bid(1, 2.45, null, TestData.USER1, 1));

        assertTrue(exception.getMessage().contains("timestamp"));
    }

    @Test
    @DisplayName("When the quantity is 0")
    public void quantityIsZero()
    {
        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> new Bid(0, 2.45, Instant.now(), TestData.USER1, 1));

        assertTrue(exception.getMessage().contains("quantity"));
    }

    @Test
    @DisplayName("Creating a valid bid")
    public void createValidBid()
    {
        final Instant now = Instant.now();
        final Bid bid = new Bid(100, 15.5, now, TestData.USER1, 2);

        assertNotNull(bid);
        assertEquals(100, bid.getQuantity());
        assertEquals(15.5, bid.getPrice());
        assertEquals(now, bid.getTimestamp());
        assertEquals(TestData.USER1, bid.getUser());
        assertEquals(2, bid.getAuctionId());
    }
}
