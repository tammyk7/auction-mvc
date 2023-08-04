package com.weareadaptive.auction.model;

public record LostBid(int auctionLotId, String symbol, int quantity, double price) {
}
