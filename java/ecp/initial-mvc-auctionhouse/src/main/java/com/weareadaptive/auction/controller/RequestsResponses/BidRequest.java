package com.weareadaptive.auction.controller.RequestsResponses;

public record BidRequest(int quantity, double minPrice, int userId)
{
}
