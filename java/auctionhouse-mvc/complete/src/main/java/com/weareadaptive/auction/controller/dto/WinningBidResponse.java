package com.weareadaptive.auction.controller.dto;

public record WinningBidResponse(
    int quantity,
    BidResponse originalBid) {
}
