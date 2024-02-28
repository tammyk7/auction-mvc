package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.RequestsResponses.BidRequest;
import com.weareadaptive.auction.RequestsResponses.CreateAuctionRequest;
import com.weareadaptive.auction.TestData;
import com.weareadaptive.auction.bid.BidRepository;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceTest
{
    @InjectMocks
    private AuctionService auctionService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AuctionRepository auctionRepository;


    @Test
    @DisplayName("Create auction")
    public void createAuction()
    {
        final User user = new User(
                "username3",
                "password3",
                "First3",
                "Last3",
                "Org3");

        when(
                userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        final Auction expectedAuction = new Auction(
                user,
                TestData.AAPL,
                100,
                10.0);

        when(
                auctionRepository.save(any(Auction.class)))
                .thenReturn(expectedAuction);

        final CreateAuctionRequest request = new CreateAuctionRequest(
                user.getId(),
                TestData.AAPL,
                100,
                10.0
        );

        final Auction createdAuction = auctionService.create(request);

        assertNotNull(createdAuction);
        assertEquals(TestData.AAPL, createdAuction.getSymbol());
        verify(auctionRepository).save(any(Auction.class));
    }

    @Test
    @DisplayName("should throw exception when creating an auction with a non existent user")
    public void shouldThrowExceptionWhenCreatingAuctionWithNonExistentUser()
    {
        final CreateAuctionRequest request = new CreateAuctionRequest(
                -1,
                TestData.AAPL,
                100,
                10.0
        );

        when(
                userRepository.findById(-1))
                .thenReturn(Optional.empty());

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
        final User user = new User(
                "username3",
                "password3",
                "First3",
                "Last3",
                "Org3");

        when(
                userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        final Auction auction = auctionService.create(
                new CreateAuctionRequest(
                        user.getId(),
                        TestData.AAPL,
                        100,
                        10.0
                )
        );

        when(
                auctionRepository.findById(auction.getId()))
                .thenReturn(Optional.of(auction));

        final BidRequest bidRequest = new BidRequest(1, 50, user.getId());

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

        when(
                auctionRepository.findById(3))
                .thenReturn(Optional.empty());

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
        final User user = new User(
                "username3",
                "password3",
                "First3",
                "Last3",
                "Org3");

        when(
                userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        final Auction createdAuction = auctionService.create(
                new CreateAuctionRequest(
                        user.getId(),
                        TestData.AAPL,
                        100,
                        10.0
                )
        );

        when(
                auctionRepository.findById(createdAuction.getId()))
                .thenReturn(Optional.of(createdAuction));

        final Auction retrievedAuction = auctionService.getAuctionById(createdAuction.getId());

        assertNotNull(retrievedAuction);
        assertEquals(TestData.AAPL, retrievedAuction.getSymbol());
    }

    @Test
    @DisplayName("should throw exception when getting a non existent auction by id")
    public void shouldThrowExceptionWhenGettingNonExistentAuctionById()
    {
        final int nonExistentAuctionId = 3;

        when(
                auctionRepository.findById(nonExistentAuctionId))
                .thenReturn(Optional.empty());

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
        final User user = new User(
                "username3",
                "password3",
                "First3",
                "Last3",
                "Org3");

        when(
                userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        final Auction auction = auctionService.create(
                new CreateAuctionRequest(
                        user.getId(),
                        TestData.AAPL,
                        100,
                        10.0
                )
        );

        when(
                auctionRepository.findById(auction.getId()))
                .thenReturn(Optional.of(auction));


        auctionService.closeAuction(auction.getId());

        assertEquals(Auction.AuctionStatus.CLOSED, auction.getStatus());
        verify(auctionRepository, times(2)).save(auction);
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
        final User user = new User(
                "username3",
                "password3",
                "First3",
                "Last3",
                "Org3");

        when(
                userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        final Auction auction = auctionService.create(
                new CreateAuctionRequest(
                        user.getId(),
                        TestData.AAPL,
                        100,
                        10.0
                )
        );

        when(
                auctionRepository.findById(auction.getId()))
                .thenReturn(Optional.of(auction));

        auctionService.removeAuction(auction.getId());

        verify(auctionRepository).deleteById(auction.getId());
    }

    @Test
    @DisplayName("should throw an exception when removing a non existent auction")
    public void shouldThrowAnExceptionWhenRemovingNonExistentAuction()
    {
        final int nonExistentAuctionId = 3;

        when(auctionRepository.findById(nonExistentAuctionId))
                .thenReturn(Optional.empty());

        final AuthenticationExceptionHandling.BusinessException exception = assertThrows(
                AuthenticationExceptionHandling.BusinessException.class,
                () -> auctionService.removeAuction(nonExistentAuctionId)
        );

        assertTrue(exception.getMessage().contains("auction does not exist"));
    }
}
