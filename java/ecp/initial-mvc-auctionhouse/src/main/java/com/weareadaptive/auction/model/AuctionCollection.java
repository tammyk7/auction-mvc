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

    public Auction getAuction(final int id)
    {
        return get(id);
    }
}
