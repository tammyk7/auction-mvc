package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.RequestsResponses.BidRequest;
import com.weareadaptive.auction.RequestsResponses.CreateAuctionRequest;
import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.bid.BidRepository;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.user.User;
import com.weareadaptive.auction.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuctionService
{
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;

    public AuctionService(final AuctionRepository auctionRepository, final UserRepository userRepository,
                          final BidRepository bidRepository)
    {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.bidRepository = bidRepository;
    }

    public Auction create(final CreateAuctionRequest createAuctionRequest)
    {
        final User user = userRepository.findById(createAuctionRequest.userId())
                .orElseThrow(() ->
                        new AuthenticationExceptionHandling.BusinessException("user does not exist")
                );

        final Auction auction = new Auction(
                user,
                createAuctionRequest.symbol(),
                createAuctionRequest.quantity(),
                createAuctionRequest.minPrice()
        );

        auctionRepository.save(auction);

        return auction;
    }

    public List<Auction> getAll()
    {
        return auctionRepository.findAll();
    }

    public Auction getAuctionById(final int auctionId)
    {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() ->
                        new AuthenticationExceptionHandling.BusinessException("auction does not exist")
                );
    }

    public void closeAuction(final int auctionId)
    {
        final Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() ->
                        new AuthenticationExceptionHandling.BusinessException("auction does not exist")
                );
        auction.close();
        auctionRepository.save(auction);
    }

    public void removeAuction(final int auctionId)
    {
        final Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() ->
                        new AuthenticationExceptionHandling.BusinessException("auction does not exist"));

        auctionRepository.deleteById(auction.getId());
    }

    public void bidOnAuction(final int auctionId, final BidRequest bidRequest)
    {
        final Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() ->
                        new AuthenticationExceptionHandling.BusinessException("auction does not exist")
                );

        final User user = userRepository.findById(bidRequest.userId())
                .orElseThrow(() ->
                        new AuthenticationExceptionHandling.BusinessException("user does not exist")
                );

        final Bid bid = new Bid(
                bidRequest.quantity(),
                bidRequest.minPrice(),
                Instant.now(),
                user,
                auction.getId()
        );

        bidRepository.save(bid);
    }

    public List<Bid> getWinningBids(final int auctionId, final int userId)
    {
        final Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() ->
                        new AuthenticationExceptionHandling.BusinessException("auction does not exist")
                );

        final User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new AuthenticationExceptionHandling.BusinessException("user does not exist")
                );
        return auction.getWinningBids()
                .stream()
                .filter(bid -> bid.getUser().equals(user))
                .toList();
    }
}
