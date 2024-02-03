package com.weareadaptive.auction.model;

public record BidLost(int auctionId, String symbol, int quantity, double price)
{
}
