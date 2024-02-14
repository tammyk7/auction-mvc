package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.RequestsResponses.BidRequest;
import com.weareadaptive.auction.RequestsResponses.CreateAuctionRequest;
import com.weareadaptive.auction.auction.Auction;
import com.weareadaptive.auction.auction.AuctionCollection;
import com.weareadaptive.auction.bid.Bid;
import com.weareadaptive.auction.bid.BidCollection;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.user.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuctionService
{
    private final AuctionCollection auctionCollection;
    private final UserCollection userCollection;
    private final BidCollection bidCollection;

    public AuctionService(final AuctionCollection auctionCollection, final UserCollection userCollection,
                          final BidCollection bidCollection)
    {
        this.auctionCollection = auctionCollection;
        this.userCollection = userCollection;
        this.bidCollection = bidCollection;
    }

    public Auction create(final CreateAuctionRequest createAuctionRequest)
    {
        final User user = userCollection.getUser(createAuctionRequest.userId());
        if (user == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        final Auction auction = new Auction(
                auctionCollection.nextId(),
                user,
                createAuctionRequest.symbol(),
                createAuctionRequest.quantity(),
                createAuctionRequest.minPrice()
        );
        auctionCollection.add(auction);

        return auction;
    }

    public List<Auction> getAll()
    {
        return auctionCollection.stream().toList();
    }

    public Auction getAuctionById(final int auctionId)
    {
        if (auctionCollection.get(auctionId) == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("auction does not exist");
        }
        return auctionCollection.getAuction(auctionId);
    }

    public void closeAuction(final int auctionId)
    {
        if (auctionCollection.get(auctionId) == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("auction does not exist");
        }
        auctionCollection.get(auctionId).close();
    }

    public void removeAuction(final int auctionId)
    {
        if (auctionCollection.get(auctionId) == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("auction does not exist");
        }
        auctionCollection.removeAuction(auctionId);
    }

    public void bidOnAuction(final int auctionId, final BidRequest bidRequest)
    {
        if (auctionCollection.get(auctionId) == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("auction does not exist");
        }
        if (userCollection.getUser(bidRequest.userId()) == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }

        final User user = userCollection.getUser(bidRequest.userId());
        final Bid bid = new Bid(
                bidCollection.nextId(),
                bidRequest.quantity(),
                bidRequest.minPrice(),
                Instant.now(),
                user
        );
        auctionCollection.getAuction(auctionId).makeBid(bid);
    }

    public List<Bid> getWinningBids(final int auctionId, final int userId)
    {
        if (auctionCollection.get(auctionId) == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("auction does not exist");
        }
        if (userCollection.getUser(userId) == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user does not exist");
        }
        final Auction auction = auctionCollection.getAuction(auctionId);
        final User user = userCollection.getUser(userId);
        return auction.getWinningBids().stream()
                .filter(winningBid -> winningBid.getUser().equals(user))
                .toList();
    }
}
