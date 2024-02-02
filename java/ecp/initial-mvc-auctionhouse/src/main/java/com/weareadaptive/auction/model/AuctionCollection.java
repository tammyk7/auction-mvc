package com.weareadaptive.auction.model;

public class AuctionState extends State<Auction>
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
}
