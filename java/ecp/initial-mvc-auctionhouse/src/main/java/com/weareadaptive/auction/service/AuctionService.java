package com.weareadaptive.auction.service;

import com.weareadaptive.auction.model.Auction;
import com.weareadaptive.auction.model.AuctionCollection;
import com.weareadaptive.auction.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionService
{
    private final AuctionCollection auctionCollection;

    public AuctionService(final AuctionCollection auctionCollection)
    {
        this.auctionCollection = auctionCollection;
    }

    //createAuction
    public Auction create(final User user, final String symbol, final int quantity, final double minPrice)
    {
        final Auction auction = new Auction(
                auctionCollection.nextId(),
                user,
                symbol,
                quantity,
                minPrice);
        auctionCollection.add(auction);
        return auction;
    }

    //getAll
    public List<Auction> getAll()
    {
        return auctionCollection.stream().toList();
    }

    //getAuctionById

    public Auction getAuctionById(final int id)
    {
        return auctionCollection.get(id);
    }

    //getAllBids
    //CloseAuction
    //GetClosingSummary

}
