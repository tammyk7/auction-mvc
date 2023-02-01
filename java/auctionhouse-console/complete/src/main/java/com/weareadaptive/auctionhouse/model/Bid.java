package com.weareadaptive.auctionhouse.model;

import java.time.Instant;

public record Bid(User user, int quantity, double price, Instant createdDate) {
  public Bid {
    if (user == null) {
      throw new BusinessException("user cannot be null");
    }

    if (price <= 0) {
      throw new BusinessException("price must be above 0");
    }

    if (quantity <= 0) {
      throw new BusinessException("quantity must be above 0");
    }

    if (createdDate == null) {
      throw new BusinessException("createdDate cannot be null");
    }
  }

  @Override
  public String toString() {
    return "Bid{"
        + "user=" + user
        + ", price=" + price
        + ", quantity=" + quantity
        + '}';
  }
}
