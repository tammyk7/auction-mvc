package com.weareadaptive.auctionhouse.model;

import java.util.List;

public class AuctionState extends State<AuctionLot> {
  public List<LostBid> findLostBids(User user) {
    if (user == null) {
      throw new IllegalArgumentException("user cannot be null");
    }
    return stream()
        .filter(auctionLot -> AuctionLot.Status.CLOSED == auctionLot.getStatus())
        .flatMap(auctionLot -> auctionLot.getLostBids(user).stream()
            .map(b -> new LostBid(
                auctionLot.getId(),
                auctionLot.getSymbol(),
                b.quantity(),
                b.price()))
        ).toList();
  }

  public List<WonBid> findWonBids(User user) {
    if (user == null) {
      throw new IllegalArgumentException("user cannot be null");
    }
    return stream()
        .filter(auctionLot -> AuctionLot.Status.CLOSED == auctionLot.getStatus())
        .flatMap(auctionLot -> auctionLot.getWonBids(user).stream()
            .map(winningBod -> new WonBid(
                auctionLot.getId(),
                auctionLot.getSymbol(),
                winningBod.quantity(),
                winningBod.originalBid().quantity(),
                winningBod.originalBid().price()))
        ).toList();
  }
}
