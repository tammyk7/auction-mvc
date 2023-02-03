package com.weareadaptive.auction.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Bid {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int id;
  private int auctionLotId;
  private String username;
  private int quantity;
  private double price;
  @Enumerated(EnumType.STRING)
  private State state;
  private int winQuantity;

  public Bid(int auctionLotId, String username, int quantity, double price) {
    this();
    if (username == null) {
      throw new BusinessException("user cannot be null");
    }

    if (price <= 0) {
      throw new BusinessException("price must be above 0");
    }

    if (quantity <= 0) {
      throw new BusinessException("quantity must be above 0");
    }

    this.price = price;
    this.username = username;
    this.quantity = quantity;
    this.auctionLotId = auctionLotId;
  }

  public Bid() {
    state = State.PENDING;
  }

  public int getQuantity() {
    return quantity;
  }

  public double getPrice() {
    return price;
  }

  public int getWinQuantity() {
    return winQuantity;
  }

  public State getState() {
    return state;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void setWinQuantity(int winQuantity) {
    this.winQuantity = winQuantity;
  }
  /*
  public void lost() {
    if (state != State.PENDING) {
      throw new BusinessException("Must be a pending bid");
    }

    state = State.LOST;
  }

  public void win(int winQuantity) {
    if (state != State.PENDING) {
      throw new BusinessException("Must be a pending bid");
    }

    if (quantity < winQuantity) {
      throw new BusinessException("winQuantity must be lower or equal to to the bid quantity");
    }

    state = State.WIN;
    this.winQuantity = winQuantity;
  }

   */

  @Override
  public String toString() {
    return "Bid{"
      + "user=" + username
      + ", price=" + price
      + ", quantity=" + quantity
      + '}';
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAuctionLotId() {
    return auctionLotId;
  }

  public void setAuctionLotId(int auctionLotId) {
    this.auctionLotId = auctionLotId;
  }

  public enum State {
    PENDING,
    LOST,
    WIN
  }
}
