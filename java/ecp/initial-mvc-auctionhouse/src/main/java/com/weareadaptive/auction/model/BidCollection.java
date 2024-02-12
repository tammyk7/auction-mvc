package com.weareadaptive.auction.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BidCollection extends State<Bid>
{
    public List<Bid> getUserBids(final int userId)
    {
        return stream()
                .filter(bid -> bid.getUser().getId() == userId)
                .toList();
    }
}
