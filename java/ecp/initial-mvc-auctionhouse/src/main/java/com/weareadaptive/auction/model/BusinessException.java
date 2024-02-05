package com.weareadaptive.auction.model;

public class BusinessException extends RuntimeException
{
    public BusinessException(final String message)
    {
        super(message);
    }
}
