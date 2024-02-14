package com.weareadaptive.auction.RequestsResponses;

public record CreateAuctionRequest(int userId, String symbol, int quantity, double minPrice)
{
}
