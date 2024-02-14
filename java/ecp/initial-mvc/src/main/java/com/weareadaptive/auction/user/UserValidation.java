package com.weareadaptive.auction.user;

public class UserValidation
{
    public static boolean isAlphanumeric(final String username)
    {
        String pattern = "^[a-zA-Z0-9]*$";
        return username.matches(pattern);
    }
}
