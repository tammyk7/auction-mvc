package com.weareadaptive.auction.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuctionCollection extends State<Auction>
{
    public void addAuction(final Auction auction)
    {
        add(auction);
    }

    public void removeAuction(final int auctionId)
    {
        remove(auctionId);
    }

    public void closeAuction(final int id)
    {
        get(id).closeAuction();
    }

    public List<BidLost> findLostBids(final User user)
    {
        if (user == null)
        {
            throw new BusinessException("user cannot be null");
        }
        return stream()
                .filter(auction -> Auction.AuctionStatus.CLOSED == auction.getStatus())
                .flatMap(auction -> auction.getLostBids(user).stream()
                        .map(lostBid -> new BidLost(
                                auction.getId(),
                                auction.getSymbol(),
                                lostBid.quantity(),
                                lostBid.price()))
                ).toList();
    }

    public List<BidWon> findWonBids(final User user)
    {
        if (user == null)
        {
            throw new BusinessException("user cannot be null");
        }
        return stream()
                .filter(auction -> Auction.AuctionStatus.CLOSED == auction.getStatus())
                .flatMap(auction -> auction.getWinningBids(user).stream()
                        .map(winningBid -> new BidWon(
                                auction.getId(),
                                auction.getSymbol(),
                                winningBid.quantity(),
                                winningBid.bid().quantity(),
                                winningBid.bid().price()))
                ).toList();
    }
}
