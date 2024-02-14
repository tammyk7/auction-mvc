package com.weareadaptive.auction.services;

import com.weareadaptive.auction.RequestsResponses.BidRequest;
import com.weareadaptive.auction.RequestsResponses.CreateAuctionRequest;
import com.weareadaptive.auction.auction.Auction;
import com.weareadaptive.auction.auction.AuctionCollection;
import com.weareadaptive.auction.bid.BidCollection;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.auction.AuctionService;
import com.weareadaptive.auction.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionServiceTest
{
    private AuctionService auctionService;

    @BeforeEach
    public void beforeEach()
    {
        final AuctionCollection auctionCollection = new AuctionCollection();
        final UserCollection userCollection = new UserCollection();
        final BidCollection bidCollection = new BidCollection();

        auctionService = new AuctionService(auctionCollection, userCollection, bidCollection);

        final User user = new User(
                1,
                "username1",
                "password",
                "First",
                "Last",
                "Company"
        );

        final User user2 = new User(
                2,
                "Username2",
                "password",
                "First",
                "Last",
                "Company"
        );

        userCollection.add(user);
        userCollection.add(user2);
    }

    @Test
    @DisplayName("Create auction")
    public void createAuction()
    {
        final CreateAuctionRequest request = new CreateAuctionRequest(
                1,
                "AAPL",
                100,
                10.0
        );
        final Auction createdAuction = auctionService.create(request);

        assertNotNull(createdAuction);
        assertEquals("AAPL", createdAuction.getSymbol());
    }

    @Test
    @DisplayName("should throw exception when creating an auction with a non existent user")
    public void shouldThrowExceptionWhenCreatingAuctionWithNonExistentUser()
    {
        final CreateAuctionRequest request = new CreateAuctionRequest(
                -1,
                "AAPL",
                100,
                10.0
        );

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auctionService.create(request)
        );

        assertTrue(exception.getMessage().contains("user does not exist"));
    }

    @Test
    @DisplayName("Bid on auction")
    public void bidOnAuction()
    {
        final Auction auction = auctionService.create(
                new CreateAuctionRequest(
                        1,
                        "AAPL",
                        100,
                        10.0
                )
        );
        final BidRequest bidRequest = new BidRequest(1, 50, 2);

        assertDoesNotThrow(() -> auctionService.bidOnAuction(auction.getId(), bidRequest));
    }

    @Test
    @DisplayName("should throw an exception when bidding on auction that does not exist")
    public void shouldThrowExceptionWhenBiddingOnNonExistentAuction()
    {
        final BidRequest bidRequest = new BidRequest(
                1,
                50,
                11
        );

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auctionService.bidOnAuction(3, bidRequest)
        );

        assertTrue(exception.getMessage().contains("auction does not exist"));
    }

    @Test
    @DisplayName("Get auction by auction id")
    public void getAuctionById()
    {
        final Auction createdAuction = auctionService.create(
                new CreateAuctionRequest(
                        1,
                        "AAPL",
                        100,
                        10.0
                )
        );

        final Auction retrievedAuction = auctionService.getAuctionById(createdAuction.getId());

        assertNotNull(retrievedAuction);
        assertEquals("AAPL", retrievedAuction.getSymbol());
    }

    @Test
    @DisplayName("should throw exception when getting a non existent auction by id")
    public void shouldThrowExceptionWhenGettingNonExistentAuctionById()
    {
        final int nonExistentAuctionId = 3;

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auctionService.getAuctionById(nonExistentAuctionId)
        );

        assertTrue(exception.getMessage().contains("auction does not exist"));
    }

    @Test
    @DisplayName("Close auction")
    public void closeAuction()
    {
        final Auction auction = auctionService.create(
                new CreateAuctionRequest(
                        1,
                        "AAPL",
                        100,
                        10.0
                )
        );

        assertDoesNotThrow(() -> auctionService.closeAuction(auction.getId()));
    }

    @Test
    @DisplayName("should throw an exception when closing a non existent auction")
    public void shouldThrowAnExceptionWhenClosingNonExistentAuction()
    {
        final int nonExistentAuctionId = 3;

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auctionService.closeAuction(nonExistentAuctionId)
        );

        assertTrue(exception.getMessage().contains("auction does not exist"));
    }

    @Test
    @DisplayName("Remove auction")
    public void removeAuction()
    {
        final Auction auction = auctionService.create(
                new CreateAuctionRequest(
                        1,
                        "AAPL",
                        100,
                        10.0
                )
        );

        assertDoesNotThrow(() -> auctionService.removeAuction(auction.getId()));
    }

    @Test
    @DisplayName("should throw an exception when removing a non existent auction")
    public void shouldThrowAnExceptionWhenRemovingNonExistentAuction()
    {
        final int nonExistentAuctionId = 3;

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auctionService.removeAuction(nonExistentAuctionId)
        );

        assertTrue(exception.getMessage().contains("auction does not exist"));
    }
}
