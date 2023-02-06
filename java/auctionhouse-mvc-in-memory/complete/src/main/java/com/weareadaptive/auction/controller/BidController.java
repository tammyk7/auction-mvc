package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.dto.BidResponse;
import com.weareadaptive.auction.service.AuctionLotService;
import java.security.Principal;
import java.util.stream.Stream;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bids")
@PreAuthorize("hasRole('ROLE_USER')")
public class BidController {
  private AuctionLotService auctionLotService;

  public BidController(AuctionLotService auctionLotService) {
    this.auctionLotService = auctionLotService;
  }

  @GetMapping
  public Stream<BidResponse> getAll(Principal user) {
    return auctionLotService.getBidsForUser(user.getName())
        .map(Mapper::map);
  }
}
