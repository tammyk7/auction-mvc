package com.adaptive.streams.exercises;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;


/***
 * Implement the following contract methods using the java.util.stream api.
 */
public class StockStreams {
  public record Stock(String symbol, BigDecimal price, Instant lastTradeTime) {
  }

  /**
   * Return true if symbol is in the provided stock collection
   */
  public boolean containsSymbol(String symbol, Collection<Stock> stocks) {
    return false;
  }

  /**
   * Return the distinct stock symbols included in the provided stock collection
   */
  public Collection<String> listDistinctSymbols(Collection<Stock> stocks) {
    return null;
  }

  /**
   * Return true if all the stocks provided have a minimum price
   */
  public boolean stocksHaveMinimumPrice(BigDecimal minPrice, Collection<Stock> stocks) {
    return false;
  }

  /**
   * Return the highest price of stock in the provided stock collection
   */
  public BigDecimal findHighestPrice(Collection<Stock> stocks) {
    return null;
  }

  /**
   * Return the last price of a given symbol in the provided stock collection
   */
  public Optional<BigDecimal> findLastPrice(String symbol, Collection<Stock> stocks) {
    return null;
  }
}

