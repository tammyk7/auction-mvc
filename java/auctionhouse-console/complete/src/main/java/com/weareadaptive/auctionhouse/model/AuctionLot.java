package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.StringUtil.isNullOrEmpty;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.reverseOrder;
import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;

import com.weareadaptive.auctionhouse.TimeContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AuctionLot implements Model {
  private final int id;
  private final User owner;
  private final String symbol;
  private final double minPrice;
  private final int quantity;
  private final List<Bid> bids;
  private Status status;
  private ClosingSummary closingSummary;

  public AuctionLot(int id, User owner, String symbol, int quantity, double minPrice) {
    if (owner == null) {
      throw new BusinessException("owner cannot be null");
    }
    if (isNullOrEmpty(symbol)) {
      throw new BusinessException("symbol cannot be null or empty");
    }
    if (minPrice < 0) {
      throw new BusinessException("minPrice cannot be bellow 0");
    }
    if (quantity < 0) {
      throw new BusinessException("quantity must be above 0");
    }
    this.id = id;
    this.owner = owner;
    this.symbol = symbol.toUpperCase().trim();
    this.quantity = quantity;
    this.minPrice = minPrice;
    bids = new ArrayList<>();
    status = Status.OPENED;
  }

  public Status getStatus() {
    return status;
  }

  public User getOwner() {
    return owner;
  }

  public String getSymbol() {
    return symbol;
  }

  public ClosingSummary getClosingSummary() {
    if (Status.CLOSED != status) {
      throw new BusinessException("AuctionLot must be closed to have a closing summary");
    }
    return closingSummary;
  }

  public List<Bid> getBids() {
    return unmodifiableList(bids);
  }

  public void bid(User bidder, int quantity, double price) {
    if (status == Status.CLOSED) {
      throw new BusinessException("Cannot close an already closed.");
    }

    if (bidder == owner) {
      throw new BusinessException("User cannot bid on his own auctions");
    }

    if (quantity < 0) {
      throw new BusinessException("quantity must be be above 0");
    }

    if (price < minPrice) {
      throw new BusinessException(format("price needs to be above %s", minPrice));
    }

    bids.add(new Bid(bidder, quantity, price, TimeContext.timeProvider().now()));
  }

  public void close() {
    if (status == Status.CLOSED) {
      throw new BusinessException("Cannot close because already closed.");
    }

    status = Status.CLOSED;

    //Don't need to use createdDate, ArrayList will iterate in the bid order automatically
    var orderedBids = bids
        .stream()
        .sorted(reverseOrder(comparing(Bid::price)))
        .toList();
    var availableQuantity = this.quantity;
    var revenue = BigDecimal.ZERO;
    var winningBids = new ArrayList<WinningBid>();
    var bidEnumerable = orderedBids.listIterator();

    while (bidEnumerable.hasNext() && availableQuantity > 0) {
      var bid = bidEnumerable.next();
      var bidQuantity = min(availableQuantity, bid.quantity());

      winningBids.add(new WinningBid(bidQuantity, bid));
      availableQuantity -= bidQuantity;
      revenue = revenue.add(valueOf(bidQuantity).multiply(valueOf(bid.price())));
    }

    closingSummary =
        new ClosingSummary(unmodifiableList(winningBids), this.quantity - availableQuantity,
            revenue, TimeContext.timeProvider().now());
  }

  public int getId() {
    return id;
  }

  public List<Bid> getLostBids(User user) {
    return bids
        .stream()
        .filter(bid -> bid.user() == user
            && closingSummary.winningBids().stream().noneMatch(wb -> wb.originalBid() == bid))
        .toList();
  }

  public List<WinningBid> getWonBids(User user) {
    return closingSummary.winningBids()
        .stream()
        .filter(b -> b.originalBid().user() == user)
        .toList();
  }

  @Override
  public String toString() {
    return "AuctionLot{"
        + "owner=" + owner
        + ", title='" + symbol + '\''
        + ", status=" + status
        + '}';
  }

  public enum Status {
    OPENED,
    CLOSED
  }
}
