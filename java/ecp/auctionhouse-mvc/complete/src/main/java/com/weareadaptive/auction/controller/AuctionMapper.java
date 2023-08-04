package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.AuctionLotResponse;
import com.weareadaptive.auction.controller.dto.BidResponse;
import com.weareadaptive.auction.controller.dto.ClosingSummaryResponse;
import com.weareadaptive.auction.controller.dto.WinningBidResponse;
import com.weareadaptive.auction.model.AuctionLot;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.model.ClosingSummary;
import com.weareadaptive.auction.model.WinningBid;

public class AuctionMapper {
  private AuctionMapper() {
  }

  public static ClosingSummaryResponse map(ClosingSummary closingSummary) {
    return new ClosingSummaryResponse(
        closingSummary.winBids().stream().map(AuctionMapper::map).toList(),
        closingSummary.totalSoldQuantity(),
        closingSummary.totalRevenue(),
        closingSummary.closedAt().toEpochMilli());
  }

  public static WinningBidResponse map(WinningBid winningBid) {
    return new WinningBidResponse(
        winningBid.quantity(),
        map(winningBid.originalBid())
    );
  }

  public static BidResponse map(Bid bid) {
    return new BidResponse(
        bid.getUsername(),
        bid.getQuantity(),
        bid.getPrice(),
        bid.getState(),
        bid.getWinQuantity());
  }

  public static AuctionLotResponse map(AuctionLot auctionLot) {
    return new AuctionLotResponse(
        auctionLot.getId(),
        auctionLot.getOwner(),
        auctionLot.getSymbol(),
        auctionLot.getMinPrice(),
        auctionLot.getQuantity(),
        auctionLot.getStatus(),
        auctionLot.getClosedAt());
  }
}
