package com.weareadaptive.auction.controller.dto;

import com.weareadaptive.auction.model.AuctionLot;
import java.time.Instant;

public record AuctionLotResponse(
    int id,
    String owner,
    String symbol,
    double minPrice,
    int quantity,
    AuctionLot.Status status,
    Instant closedAt
) {
}
