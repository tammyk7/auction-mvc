package com.weareadaptive.auction.service;

import static java.lang.Math.min;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;

import com.weareadaptive.auction.TimeContext;
import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.repository.AuctionLotRepository;
import com.weareadaptive.auction.repository.BidReposity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuctionLotService {
  public static final String AUCTION_LOT_ENTITY = "AuctionLot";
  private final AuctionLotRepository auctionLotRepository;
  private final BidReposity bidReposity;

  public AuctionLotService(AuctionLotRepository auctionLotRepository,
                           BidReposity bidReposity) {
    this.auctionLotRepository = auctionLotRepository;
    this.bidReposity = bidReposity;
  }

  public Optional<AuctionLot> get(int id) {
    return auctionLotRepository.findById(id);
  }

  public AuctionLot create(String owner, String symbol, double minPrice, int quantity) {
    var auctionLot = new AuctionLot(
        owner,
        symbol,
        quantity,
        minPrice
    );
    auctionLotRepository.save(auctionLot);
    return auctionLot;
  }

  public void bid(String username, int auctionLotId, int quantity, double price) {
    var auctionLot = auctionLotRepository.findById(auctionLotId);

    if (auctionLot.isEmpty()) {
      throw new NotFoundException(AUCTION_LOT_ENTITY, auctionLotId);
    }

    if (auctionLot.get().getOwner().equals(username)) {
      throw new BusinessException("user cannot bid on his auction");
    }

    var bid = new Bid(auctionLotId, username, quantity, price);
    bidReposity.save(bid);
  }

  public List<AuctionLot> getAll() {
    return auctionLotRepository.findAll();
  }

  @Transactional
  public ClosingSummary close(String username, int auctionLotId) {
    var auctionLot = auctionLotRepository.findById(auctionLotId)
        .orElseThrow(() -> new NotFoundException(AUCTION_LOT_ENTITY, auctionLotId));

    if (!auctionLot.getOwner().equals(username)) {
      throw new AccessDeniedException("Only the owner close an auction");
    }

    if (auctionLot.getStatus() == AuctionLot.Status.CLOSED) {
      throw new BusinessException("Cannot close because already closed.");
    }

    var auctionBids = bidReposity.getBidsForAuction(auctionLotId);
    //TODO: Can be much better. It is pretty intense for get everything from the DB
    var orderedBids = auctionBids
        .stream()
        .sorted(reverseOrder(comparing(Bid::getPrice))
            .thenComparing(reverseOrder(comparingInt(Bid::getQuantity))))
        .toList();
    var availableQuantity = auctionLot.getQuantity();
    var now = TimeContext.timeProvider().now();
    var revenue = BigDecimal.ZERO;
    auctionLot.setClosedAt(now);
    auctionLot.setStatus(AuctionLot.Status.CLOSED);
    var winBids = new ArrayList<Bid>();

    for (Bid bid : orderedBids) {
      if (availableQuantity > 0) {
        bid.setState(Bid.State.WIN);
        var bidQuantity = min(availableQuantity, bid.getQuantity());
        bid.setWinQuantity(bidQuantity);
        availableQuantity -= bidQuantity;
        revenue = revenue.add(valueOf(bidQuantity).multiply(valueOf(bid.getPrice())));
        winBids.add(bid);
      } else {
        bid.setState(Bid.State.LOST);
      }
    }
    auctionLotRepository.save(auctionLot);
    bidReposity.saveAll(auctionBids);
    return createClosingSummary(auctionLot, winBids);

  }

  public ClosingSummary getClosingSummary(String username, int auctionLotId) {
    var auctionLot = auctionLotRepository.findById(auctionLotId)
        .orElseThrow(NotFoundException::new);

    if (!auctionLot.getOwner().equals(username)) {
      throw new AccessDeniedException("Only owner can access the closing summary");
    }

    if (auctionLot.getStatus() != AuctionLot.Status.CLOSED) {
      throw new NotFoundException();
    }

    var winBids = bidReposity.getWinBidsForAuction(auctionLotId);
    return createClosingSummary(auctionLot, winBids);
  }

  private ClosingSummary createClosingSummary(AuctionLot auctionLot, List<Bid> winBids) {
    var revenue = winBids.stream()
        .map(b -> valueOf(b.getWinQuantity()).multiply(valueOf(b.getPrice())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    var soldQuantity = winBids
        .stream()
        .map(Bid::getWinQuantity)
        .reduce(0, Integer::sum);
    return new ClosingSummary(
        winBids,
        soldQuantity,
        revenue,
        auctionLot.getClosedAt()
    );
  }

  public List<Bid> getBidsForUser(String username) {
    return bidReposity.getBidsForUser(username);
  }

  public List<Bid> getAuctionLotBids(int auctionLotId) {
    if (!auctionLotRepository.existsById(auctionLotId)) {
      throw new NotFoundException(AUCTION_LOT_ENTITY, auctionLotId);
    }

    return bidReposity.getBidsForAuction(auctionLotId);
  }

  public Optional<AuctionLot> getAuctionLotById(int auctionLotId) {
    return auctionLotRepository.findById(auctionLotId);
  }
}
