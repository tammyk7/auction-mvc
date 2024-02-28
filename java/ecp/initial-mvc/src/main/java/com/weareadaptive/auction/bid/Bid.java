package com.weareadaptive.auction.bid;

import com.weareadaptive.auction.auction.Auction;
import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "bid")
public class Bid
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private int quantity;
    @Column(columnDefinition = "numeric")
    private double price;
    private Instant timestamp;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "auction_id")
    private int auctionId;

    public Bid(final int quantity, final double minPrice, final Instant timestamp, final User user,
               final int auctionId)
    {
        if (timestamp == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("timestamp cannot be null");
        }
        if (quantity <= 0)
        {
            throw new AuthenticationExceptionHandling.BusinessException("quantity has to be above 0");
        }
        if (user == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("user cannot be null");
        }

        this.quantity = quantity;
        this.price = minPrice;
        this.timestamp = timestamp;
        this.user = user;
        this.auctionId = auctionId;
    }

    public Bid()
    {

    }

    @Override
    public String toString()
    {
        return "Bid{" + "quantity=" + quantity + ", price=" + price + ", user='" + user + '\'' + '}';
    }

    public int getAuctionId()
    {
        return auctionId;
    }

    public int getId()
    {
        return id;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public double getPrice()
    {
        return price;
    }

    public Instant getTimestamp()
    {
        return timestamp;
    }

    public User getUser()
    {
        return user;
    }

    public enum BidStatus
    {
        WON, LOSS, PENDING, CANCELLED
    }

}
