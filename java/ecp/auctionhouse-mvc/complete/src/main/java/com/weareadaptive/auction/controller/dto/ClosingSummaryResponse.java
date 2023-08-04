package com.weareadaptive.auction.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public record ClosingSummaryResponse(
    List<BidResponse> winBids,
    int totalSoldQuantity,
    BigDecimal totalRevenue,
    long closedAt) {
}
