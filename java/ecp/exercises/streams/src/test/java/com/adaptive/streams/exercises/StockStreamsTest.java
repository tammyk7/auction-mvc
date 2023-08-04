package com.adaptive.streams.exercises;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StockStreamsTest {
  StockStreams streams = new StockStreams();

  List<StockStreams.Stock> stocks = getStocks();

  private List<StockStreams.Stock> getStocks() {
    Instant now = Instant.now();

    StockStreams.Stock aapl1 = new StockStreams.Stock("AAPL", BigDecimal.valueOf(125), now.plusMillis(10));
    StockStreams.Stock tsla1 = new StockStreams.Stock("TSLA", BigDecimal.valueOf(640), now.plusMillis(10));
    StockStreams.Stock aapl2 = new StockStreams.Stock("AAPL", BigDecimal.valueOf(112), now.plusMillis(12));
    StockStreams.Stock amzn = new StockStreams.Stock("AMZN", BigDecimal.valueOf(6300), now.plusMillis(1));
    StockStreams.Stock tsla2 = new StockStreams.Stock("TSLA", BigDecimal.valueOf(620), now.plusMillis(8));

    return Arrays.asList(aapl1, tsla1, aapl2, amzn, tsla2);
  }

  @Test
  public void testListDistinctSymbols() {
    assertThat(streams.listDistinctSymbols(stocks)).hasSize(3);
    assertThat(streams.listDistinctSymbols(stocks)).containsExactly("AMZN", "AAPL", "TSLA");
  }

  @Test
  public void testMinimumPrice() {
    assertThat(streams.stocksHaveMinimumPrice(BigDecimal.valueOf(100), stocks)).isTrue();
    assertThat(streams.stocksHaveMinimumPrice(BigDecimal.valueOf(1000), stocks)).isFalse();
  }

  @Test
  public void testFindHighestPrice() {
    assertThat(streams.findHighestPrice(stocks)).isEqualTo(BigDecimal.valueOf(6300));
    assertThat(streams.findHighestPrice(Collections.emptyList())).isNull();
  }

  @Test
  public void testFindLastPrice() {
    assertThat(streams.findLastPrice("AAPL", stocks).get()).isEqualTo(BigDecimal.valueOf(112));
    assertThat(streams.findLastPrice("TSLA", stocks).get()).isEqualTo(BigDecimal.valueOf(640));
    assertThat(streams.findLastPrice("AMZN", stocks).get()).isEqualTo(BigDecimal.valueOf(6300));
    assertThat(streams.findLastPrice("XYZ", stocks).isPresent()).isFalse();
  }

  @Test
  public void testHasSymbol() {
    assertThat(streams.containsSymbol("AAPL", stocks)).isTrue();
    assertThat(streams.containsSymbol("TSLA", stocks)).isTrue();
    assertThat(streams.containsSymbol("XYZ", stocks)).isFalse();
  }
}

