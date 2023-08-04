package com.weareadaptive.auctionhouse;

import java.time.Instant;

public class TimeContext {
  private static TimeProvider timeProvider;

  static {
    timeProvider = new InstantTimeProvider();
  }

  public static TimeProvider timeProvider() {
    return timeProvider;
  }

  static void setTimeProvider(TimeProvider timeProvider) {
    if (timeProvider == null) {
      throw new IllegalArgumentException("timeProvider cannot be null");
    }

    TimeContext.timeProvider = timeProvider;
  }

  @FunctionalInterface
  public interface TimeProvider {
    Instant now();
  }

  static class InstantTimeProvider implements TimeProvider {

    @Override
    public Instant now() {
      return Instant.now();
    }
  }
}
