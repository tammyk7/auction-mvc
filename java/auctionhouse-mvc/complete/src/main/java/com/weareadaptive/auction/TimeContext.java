package com.weareadaptive.auction;

import java.time.Instant;

public class TimeContext {
  private static TimeProvider timeProvider;

  static {
    timeProvider = new InstantTimeProvider();
  }

  public static TimeProvider timeProvider() {
    return timeProvider;
  }

  @FunctionalInterface
  public interface TimeProvider {
    Instant now();
  }

  static void setTimeProvider(TimeProvider timeProvider) {
    if (timeProvider == null) {
      throw new IllegalArgumentException("timeProvider cannot be null");
    }

    TimeContext.timeProvider = timeProvider;
  }

  static class InstantTimeProvider implements TimeProvider  {

    @Override
    public Instant now() {
      return Instant.now();
    }
  }
}

