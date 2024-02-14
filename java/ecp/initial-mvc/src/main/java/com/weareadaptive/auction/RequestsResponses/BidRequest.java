package com.weareadaptive.auction.RequestsResponses;

public record BidRequest(int quantity, double minPrice, int userId)
{
}
