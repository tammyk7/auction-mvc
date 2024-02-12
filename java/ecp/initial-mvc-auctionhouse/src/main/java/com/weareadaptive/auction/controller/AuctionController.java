package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.controller.RequestsResponses.AuctionResponse;
import com.weareadaptive.auction.controller.RequestsResponses.BidRequest;
import com.weareadaptive.auction.controller.RequestsResponses.CreateAuctionRequest;
import com.weareadaptive.auction.model.Auction;
import com.weareadaptive.auction.model.Bid;
import com.weareadaptive.auction.service.AuctionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController
{
    final AuctionService auctionService;

    public AuctionController(final AuctionService auctionService)
    {
        this.auctionService = auctionService;
    }

    @GetMapping("/")
    public List<AuctionResponse> getAllAuctions()
    {
        final List<Auction> auctions = auctionService.getAll();

        return auctions.stream()
                .map(auction -> new AuctionResponse(
                        auction.getSymbol(),
                        auction.getQuantity(),
                        auction.getMinPrice()
                )).toList();
    }

    @PostMapping("/")
    public AuctionResponse createAuction(@RequestBody final CreateAuctionRequest createAuctionRequest)
    {
        final Auction auctionCreated = auctionService.create(createAuctionRequest);

        return new AuctionResponse(
                auctionCreated.getSymbol(),
                auctionCreated.getQuantity(),
                auctionCreated.getMinPrice()
        );
    }

    @GetMapping("/{auctionId}")
    public AuctionResponse findByAuctionId(@PathVariable final int auctionId)
    {
        final Auction auction = auctionService.getAuctionById(auctionId);
        return new AuctionResponse(
                auction.getSymbol(),
                auction.getQuantity(),
                auction.getMinPrice()
        );
    }

    @GetMapping("/{auctionId}/{userId}/winningBids")
    public List<Bid> getWinningBids(@PathVariable final int auctionId,
                                    @PathVariable final int userId)
    {
        return auctionService.getWinningBids(auctionId, userId);
    }

    @PostMapping("/{auctionId}/bids")
    public void bidOnAuction(@PathVariable final int auctionId,
                             @RequestBody final BidRequest bidRequest)
    {
        auctionService.bidOnAuction(auctionId, bidRequest);
    }

    @PutMapping("/{auctionId}/close")
    public void closeAuction(@PathVariable final int auctionId)
    {
        auctionService.closeAuction(auctionId);
    }

    @PutMapping("/{auctionId}/remove")
    public void removeAuction(@PathVariable final int auctionId)
    {
        auctionService.removeAuction(auctionId);
    }
}
