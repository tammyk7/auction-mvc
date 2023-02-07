package com.weareadaptive.auction.controller.dto;

public record AuctionLotResponse(
    int id,
    String owner,
    String symbol,
    double minPrice,
    int quantity,
    ClosingSummaryResponse closingSummary
) {
}
