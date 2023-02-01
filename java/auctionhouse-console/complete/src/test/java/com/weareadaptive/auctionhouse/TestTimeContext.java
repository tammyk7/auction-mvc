package com.weareadaptive.auctionhouse;

import java.time.Instant;

public class TestTimeContext {
  public static void setTimeProvider(TimeContext.TimeProvider timeProvider) {
    TimeContext.setTimeProvider(timeProvider);
  }

  public static void useFixedTime(Instant timeProvider) {
    TimeContext.setTimeProvider(() -> timeProvider);
  }

  public static Instant useFixedTime() {
    var now = Instant.now();
    TimeContext.setTimeProvider(() -> now);
    return now;
  }

  public static void userNormal() {
    TimeContext.setTimeProvider(new TimeContext.InstantTimeProvider());
  }
}
