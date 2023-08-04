package com.weareadaptive.auction.exception;

public class Problem {
  public final int status;
  public final String title;
  public final String message;

  public Problem(int status, String title, String message) {
    this.status = status;
    this.title = title;
    this.message = message;
  }
}