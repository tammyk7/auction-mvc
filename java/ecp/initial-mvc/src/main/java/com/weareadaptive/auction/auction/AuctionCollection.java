package com.weareadaptive.auction.auction;

import com.weareadaptive.auction.model.State;
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

    public Auction getAuction(final int id)
    {
        return get(id);
    }
}
