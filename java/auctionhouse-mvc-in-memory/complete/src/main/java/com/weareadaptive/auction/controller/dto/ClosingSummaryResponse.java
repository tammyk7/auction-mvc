package com.weareadaptive.auction.controller.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ClosingSummaryResponse(
    List<WinningBidResponse> winningBids,
    int totalSoldQuantity,
    BigDecimal totalRevenue,
    Instant closingTime) {
}
