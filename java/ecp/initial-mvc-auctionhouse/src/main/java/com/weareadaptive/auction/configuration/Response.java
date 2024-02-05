package com.weareadaptive.auction.configuration;


public class Response<T>
{
    private final String message;
    private final T data;

    public Response(final String message, final T data)
    {
        this.message = message;
        this.data = data;
    }

    public Response(final String message)
    {
        this.message = message;
        this.data = null;
    }

    public String getMessage()
    {
        return message;
    }

    public T getData()
    {
        return data;
    }
}
