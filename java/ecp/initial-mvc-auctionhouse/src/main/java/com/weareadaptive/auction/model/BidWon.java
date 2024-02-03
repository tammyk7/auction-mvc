package com.weareadaptive.auction.model;

public record BidWon(int auctionId, String symbol, int wonQuantity, int bidQuantity, double price)
{
}
