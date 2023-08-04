package com.weareadaptive.auctionhouse;

public class StringUtil {
  private StringUtil() {
  }

  public static boolean isNullOrEmpty(String theString) {
    return theString == null || theString.isBlank();
  }
}
