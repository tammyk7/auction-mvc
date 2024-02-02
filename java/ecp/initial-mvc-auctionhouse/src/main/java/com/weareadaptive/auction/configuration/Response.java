package com.weareadaptive.auction.configuration;

public record Response<T>(String message, T data)
{
}
