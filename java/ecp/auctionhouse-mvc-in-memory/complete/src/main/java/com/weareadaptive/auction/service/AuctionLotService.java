package com.weareadaptive.auction.service;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

import com.weareadaptive.auction.exception.AuctionException;
import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.AuctionState;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.model.User;
import com.weareadaptive.auction.model.UserState;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AuctionLotService {
  public static final String AUCTION_LOT_ENTITY = "AuctionLot";
  private final AuctionState auctionState;
  private final UserState userState;

  public AuctionLotService(AuctionState auctionState, UserState userState) {
    this.auctionState = auctionState;
    this.userState = userState;
  }

  public Optional<AuctionLot> get(int id) {
    return ofNullable(auctionState.get(id));
  }

  public AuctionLot create(String owner, String symbol, double minPrice, int quantity) {
    var ownerUser = userState.getByUsername(owner)
        .orElseThrow(() -> new AuctionException(format("Invalid owner %s", owner)));

    var auctionLot = new AuctionLot(
        auctionState.nextId(),
        ownerUser,
        symbol,
        quantity,
        minPrice
    );
    auctionState.add(auctionLot);
    return auctionLot;
  }

  public void bid(String username, int auctionLotId, int quantity, double price) {
    User user = userState.getByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

    var auction = auctionState.get(auctionLotId);
    if (auction == null) {
      throw new NotFoundException(AUCTION_LOT_ENTITY, auctionLotId);
    }
    auction.bid(user, quantity, price);
  }

  public Stream<AuctionLot> getAll() {
    return auctionState.stream();
  }

  public Stream<Bid> getBidsForUser(String username) {
    return auctionState
        .stream()
        .flatMap(a -> a.getBids().stream())
        .filter(b -> b.getUser().getUsername().equals(username));
  }

  public Stream<Bid> getAuctionLotBids(int auctionLotId) {
    var auction = auctionState.get(auctionLotId);
    if (auction == null) {
      throw new NotFoundException(AUCTION_LOT_ENTITY, auctionLotId);
    }

    return auction.getBids().stream();
  }

  public ClosingSummary close(String username, int auctionLotId) {
    var auction = getAuctionLot(auctionLotId);
    if (!auction.getOwner().getUsername().equals(username)) {
      throw new AccessDeniedException("Only the owner can close");
    }
    auction.close();
    return auction.getClosingSummary();
  }

  public AuctionLot getAuctionLot(int auctionLotId) {
    return Optional.ofNullable(auctionState.get(auctionLotId))
        .orElseThrow(() -> new NotFoundException(AUCTION_LOT_ENTITY, auctionLotId));
  }
}
