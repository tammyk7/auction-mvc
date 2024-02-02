package com.weareadaptive.auction.controller;

import com.weareadaptive.auction.model.Auction;
import com.weareadaptive.auction.service.AuctionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//get all auctions
//create auctions -> symbol,minPrice,quantity and then return the auction
//based on auction id -> should return the specific auction information
// get all bids -> must be the owner
// Close an auction -> must be owner, must be open and return summary
// Get closing summary of the auction
@RestController
@RequestMapping("/auctions")
public class AuctionController
{
    AuctionService auctionService;

    public AuctionController(final AuctionService auctionService)
    {
        this.auctionService = auctionService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Auction>> getAllAuctions()
    {
        return ResponseEntity.ok().body(auctionService.getAll());
    }
}
