package com.weareadaptive.auction.model;

public record WonBid(
    int auctionLotId,
    String symbol,
    int wonQuantity,
    int bidQuantity,
    double price) {
}
