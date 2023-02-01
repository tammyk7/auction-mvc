package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.Bid;

public record BidResponse(
    String username,
    int quantity,
    double price,
    Bid.State state,
    int winQuantity
) {
}
