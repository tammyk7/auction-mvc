package com.adaptive.streams.exercises;

import static com.google.common.truth.Truth.assertThat;
import static java.util.List.of;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

public class LineItemStreamTest {
  LineItemStream streams = new LineItemStream();

  List<LineItemStream.LineItem> lineItems = getLineItems();

  private List<LineItemStream.LineItem> getLineItems() {
    return of(
      new LineItemStream.LineItem("TV", 54, BigDecimal.valueOf(12)), //648
      new LineItemStream.LineItem("Apple TV", 45, BigDecimal.valueOf(50)) // 2250
    );
  }

  @Test
  public void calculateTotal() {
    assertThat(streams.calculateTotal(lineItems)).isEqualTo(BigDecimal.valueOf(2898));
  }
}
