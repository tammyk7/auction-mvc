package com.weareadaptive.auctionhouse.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ClosingSummary(List<WinningBid> winningBids, int totalSoldQuantity,
                             BigDecimal totalRevenue,
                             Instant closingTime) {
}
