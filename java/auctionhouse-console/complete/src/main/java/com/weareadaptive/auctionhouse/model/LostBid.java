package com.weareadaptive.auctionhouse.model;

public record LostBid(int auctionLotId, String symbol, int quantity, double price) {
}
