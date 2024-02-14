package com.weareadaptive.auction.RequestsResponses;

public record UpdateUserRequest(String firstName, String lastName, String password, String organisation)
{
}
