package com.weareadaptive.auction.configuration;

import com.weareadaptive.auction.model.Auction;
import com.weareadaptive.auction.model.AuctionCollection;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserCollection;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInit
{
    private final UserCollection userCollection;
    private final AuctionCollection auctionCollection;

    public ApplicationInit(final UserCollection userCollection, final AuctionCollection auctionCollection)
    {
        this.userCollection = userCollection;
        this.auctionCollection = auctionCollection;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitData()
    {
        var admin = new User(
                userCollection.nextId(),
                "ADMIN",
                "adminpassword",
                "admin",
                "admin",
                "Adaptive",
                true);
        userCollection.add(admin);

        var aaplAuction = new Auction(
                auctionCollection.nextId(),
                userCollection.getUser(1),
                "aapl",
                20,
                30
        );
        auctionCollection.add(aaplAuction);

        System.out.println("hello tammy");
    }
}
