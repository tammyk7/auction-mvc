package com.weareadaptive.auction.RequestsResponses;

public record CreateUserRequest(String username, String password, String firstname, String lastname,
                                String organisation)
{
}
