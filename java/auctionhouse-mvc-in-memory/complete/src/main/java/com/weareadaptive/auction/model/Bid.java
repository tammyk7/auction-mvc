package com.weareadaptive.auction.model;

public class Bid {
  private final User user;
  private final int quantity;
  private final double price;
  private State state;
  private int winQuantity;

  public Bid(User user, int quantity, double price) {
    if (user == null) {
      throw new BusinessException("user cannot be null");
    }

    if (price <= 0) {
      throw new BusinessException("price must be above 0");
    }

    if (quantity <= 0) {
      throw new BusinessException("quantity must be above 0");
    }

    this.price = price;
    this.user = user;
    this.quantity = quantity;
    state = State.PENDING;
  }

  public int getQuantity() {
    return quantity;
  }

  public User getUser() {
    return user;
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

  @Override
  public String toString() {
    return "Bid{"
      + "user=" + user
      + ", price=" + price
      + ", quantity=" + quantity
      + '}';
  }

  public enum State {
    PENDING,
    LOST,
    WIN
  }
}
