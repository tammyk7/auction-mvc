package com.weareadaptive.auctionhouse.console;

public class Parser {

  private Parser() {
  }

  public static int parseInt(String value, String property) throws ParsingException {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException exception) {
      throw new ParsingException(property, "Integer");
    }
  }

  public static double parseDouble(String value, String property) throws ParsingException {
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException exception) {
      throw new ParsingException(property, "Double");
    }
  }
}
