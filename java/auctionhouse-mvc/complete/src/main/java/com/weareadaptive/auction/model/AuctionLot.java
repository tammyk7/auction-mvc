package com.weareadaptive.auction.model;

import static org.apache.logging.log4j.util.Strings.isBlank;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AuctionLot {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int id;
  private String owner;
  private String symbol;
  private double minPrice;
  private int quantity;
  private Instant closedAt;
  @Enumerated(EnumType.STRING)
  private Status status;

  public AuctionLot(String owner, String symbol, int quantity, double minPrice) {
    this();
    if (owner == null) {
      throw new BusinessException("owner cannot be null");
    }
    if (isBlank(symbol)) {
      throw new BusinessException("symbol cannot be null or empty");
    }
    if (minPrice < 0) {
      throw new BusinessException("minPrice cannot be bellow 0");
    }
    if (quantity < 0) {
      throw new BusinessException("quantity must be above 0");
    }
    this.owner = owner;
    this.symbol = symbol.toUpperCase().trim();
    this.quantity = quantity;
    this.minPrice = minPrice;
  }

  public AuctionLot() {
    status = Status.OPENED;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public void setMinPrice(double minPrice) {
    this.minPrice = minPrice;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getOwner() {
    return owner;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  /*
  public ClosingSummary getClosingSummary() {
    if (Status.CLOSED != status) {
      throw new BusinessException("AuctionLot must be closed to have a closing summary");
    }
    return closingSummary;
  }

   */

  public int getId() {
    return id;
  }

  public double getMinPrice() {
    return minPrice;
  }

  public int getQuantity() {
    return quantity;
  }

  @Override
  public String toString() {
    return "AuctionLot{"
        + "owner=" + owner
        + ", title='" + symbol + '\''
        + ", status=" + status
        + '}';
  }

  public Instant getClosedAt() {
    return closedAt;
  }

  public void setClosedAt(Instant closedAt) {
    this.closedAt = closedAt;
  }

  public enum Status {
    OPENED,
    CLOSED
  }
}
