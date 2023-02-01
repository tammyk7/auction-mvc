package com.weareadaptive.auction.controller;

import static com.weareadaptive.auction.controller.AuctionMapper.map;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.weareadaptive.auction.controller.dto.AuctionLotResponse;
import com.weareadaptive.auction.controller.dto.BidRequest;
import com.weareadaptive.auction.controller.dto.BidResponse;
import com.weareadaptive.auction.controller.dto.ClosingSummaryResponse;
import com.weareadaptive.auction.controller.dto.CreateAuctionLotRequest;
import com.weareadaptive.auction.exception.NotFoundException;
import com.weareadaptive.auction.service.AuctionLotService;
import java.security.Principal;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auction-lots")
@PreAuthorize("hasRole('ROLE_USER')")
public class AuctionLotController {
  private final AuctionLotService auctionLotService;

  public AuctionLotController(AuctionLotService auctionLotService) {
    this.auctionLotService = auctionLotService;
  }

  @GetMapping("/{id}")
  public AuctionLotResponse get(@PathVariable int id) {
    return auctionLotService.get(id)
        .map(AuctionMapper::map)
        .orElseThrow(NotFoundException::new);
  }

  @PostMapping("/{id}/bid")
  @ResponseStatus(NO_CONTENT)
  public void bid(
      @PathVariable int id,
      @RequestBody @Valid BidRequest request,
      Principal principal) {
    auctionLotService.bid(principal.getName(), id, request.quantity(), request.price());
  }

  @GetMapping("/{id}/bids")
  public Stream<BidResponse> getAuctionLotBids(@PathVariable int id) {
    return auctionLotService.getAuctionLotBids(id)
      .stream()
      .map(AuctionMapper::map);
  }

  @PostMapping("/{id}/close")
  public ClosingSummaryResponse closeAuctionLot(@PathVariable int id, Principal principal) {
    var closingSummary = auctionLotService.close(principal.getName(), id);
    return map(closingSummary);
  }

  @GetMapping("/{id}/closingSummary")
  public ClosingSummaryResponse getClosingSummary(@PathVariable int id, Principal principal) {
    return map(auctionLotService.getClosingSummary(principal.getName(), id));
  }

  @GetMapping
  public Stream<AuctionLotResponse> getAll() {
    return auctionLotService.getAll()
        .stream()
        .map(AuctionMapper::map);
  }

  @PostMapping
  @ResponseStatus(CREATED)
  public AuctionLotResponse create(
      @Valid @RequestBody CreateAuctionLotRequest request,
      Principal principal) {
    var auction = auctionLotService.create(
        principal.getName(),
        request.symbol(),
        request.minPrice(),
        request.quantity()
    );
    return map(auction);
  }
}
