package com.weareadaptive.auction.controller.RequestsResponses;

public record CreateAuctionRequest(int userId, String symbol, int quantity, double minPrice)
{
}
