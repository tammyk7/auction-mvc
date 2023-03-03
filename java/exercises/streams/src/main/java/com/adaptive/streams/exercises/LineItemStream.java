package com.adaptive.streams.exercises;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

public class LineItemStream {
  public record LineItem(String product, int quantity, BigDecimal price) {
  }

  public BigDecimal calculateTotal(Collection<LineItem> lineItems) {
    return BigDecimal.ZERO;
  }
}
