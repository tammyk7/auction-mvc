package com.weareadaptive.auction.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ClosingSummary(
    List<Bid> winBids,
    int totalSoldQuantity,
    BigDecimal totalRevenue,
    Instant closedAt) {
}
