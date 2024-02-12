package com.weareadaptive.auction.controller.RequestsResponses;

public record CreateUserRequest(String username, String password, String firstname, String lastname,
                                String organisation)
{
}
