package com.weareadaptive.auction.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CreateAuctionLotRequest(
    @NotBlank
    @Size(max = 10)
    String symbol,

    @Min(0)
    double minPrice,

    @Min(1)
    int quantity
) { }
