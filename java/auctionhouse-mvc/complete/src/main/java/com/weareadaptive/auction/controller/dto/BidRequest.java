package com.weareadaptive.auction.controller.dto;

import jakarta.validation.constraints.Min;

public record BidRequest(
    @Min(1)
    int quantity,

    @Min(0)
    double price) {
}
