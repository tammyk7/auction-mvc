package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.AuctionLotResponse;
import com.weareadaptive.auction.controller.dto.BidResponse;
import com.weareadaptive.auction.controller.dto.ClosingSummaryResponse;
import com.weareadaptive.auction.controller.dto.WinningBidResponse;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.model.WinningBid;

public class Mapper {
  public static ClosingSummaryResponse map(ClosingSummary closingSummary) {
    return new ClosingSummaryResponse(
        closingSummary.winningBids().stream().map(Mapper::map).toList(),
        closingSummary.totalSoldQuantity(),
        closingSummary.totalRevenue(),
        closingSummary.closingTime());
  }

  public static WinningBidResponse map(WinningBid winningBid) {
    return new WinningBidResponse(
        winningBid.quantity(),
        map(winningBid.originalBid())
    );
  }

  public static BidResponse map(Bid bid) {
    return new BidResponse(
        bid.getUser().getUsername(),
        bid.getQuantity(),
        bid.getPrice(),
        bid.getState(),
        bid.getWinQuantity());
  }

  public static AuctionLotResponse map(AuctionLot auctionLot) {
    return new AuctionLotResponse(
        auctionLot.getId(),
        auctionLot.getOwner().getUsername(),
        auctionLot.getSymbol(),
        auctionLot.getMinPrice(),
        auctionLot.getQuantity(),
        auctionLot.getStatus() == AuctionLot.Status.CLOSED
            ? map(auctionLot.getClosingSummary()) : null);
  }
}
