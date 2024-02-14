package com.weareadaptive.auction.bid;

import com.weareadaptive.auction.exception.AuthenticationExceptionHandling;
import com.weareadaptive.auction.model.Entity;
import com.weareadaptive.auction.user.User;

import java.time.Instant;


public class Bid implements Entity
{
    private final int id;
    private final int quantity;
    private final double price;
    private final Instant timestamp;
    private final User user;
//    private BidStatus status;

    public Bid(final int id, final int quantity, final double minPrice, final Instant timestamp, final User user)
    {
        if (timestamp == null)
        {
            throw new AuthenticationExceptionHandling.BusinessException("timestamp cannot be null");
        }
        if (quantity <= 0)
        {
            throw new AuthenticationExceptionHandling.BusinessException("quantity has to be above 0");
        }
        this.id = id;
        this.quantity = quantity;
        this.price = minPrice;
        this.timestamp = timestamp;
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "Bid{" + "quantity=" + quantity + ", price=" + price + ", user='" + user + '\'' + '}';
    }

    @Override
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
//    public BidStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(BidStatus status) {
//        this.status = status; // Allow status to be updated
//    }

    public enum BidStatus
    {
        WON, LOSS, PENDING, CANCELLED
    }

}
