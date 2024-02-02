package com.weareadaptive.auctionhouse.model;

import java.time.Instant;


public record Bid(int quantity, double price, Instant timestamp, User user)
{
    public Bid
    {
        if (timestamp == null)
        {
            throw new BusinessException("timestamp cannot be null");
        }
    }

    @Override
    public String toString()
    {
        return "Bid{" + "quantity=" + quantity + ", price=" + price + ", user='" + user + '\'' + '}';
    }
}
