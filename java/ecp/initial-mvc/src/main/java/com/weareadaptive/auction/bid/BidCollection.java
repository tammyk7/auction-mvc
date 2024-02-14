package com.weareadaptive.auction.bid;

import com.weareadaptive.auction.model.State;
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
