package com.weareadaptive.auction.model;

import org.springframework.stereotype.Component;

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

    public Auction getAuction(final int id)
    {
        return get(id);
    }
}
