package com.weareadaptive.auction.controller.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAuctionLotRequest(
    @NotBlank
    @Size(max = 10)
    String symbol,

    @Min(0)
    double minPrice,

    @Min(1)
    int quantity
) { }
