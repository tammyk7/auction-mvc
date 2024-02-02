package com.weareadaptive.auction.model;

public final class StringUtil
{
    private StringUtil()
    {
    }

    public static boolean isNullOrEmpty(final String theString)
    {
        return theString == null || theString.isBlank();
    }
}
