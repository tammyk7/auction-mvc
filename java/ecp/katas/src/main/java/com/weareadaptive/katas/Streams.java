package com.weareadaptive.katas;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;

/***
 * Implement the following contract methods using the java.util.stream api.
 */
public class Streams {

  public record Transaction(
      BigDecimal amount,
      Instant lastTradeTime,
      Direction direction,
      String category
  ) {
  }

  public enum Direction {
    Credit(-1),
    Debit(1);

    private final int multiplier;

    Direction(int multiplier) {
      this.multiplier = multiplier;
    }

    public int multiplier() {
      return multiplier;
    }
  }

  /**
   * Return true if category is in the provided transactions collection
   */
  public boolean q1_containsCategory(String category, Collection<Transaction> transactions) {
    return false;
  }

  /**
   * Return the distinct categories
   */
  public Collection<String> q2_listCategories(Collection<Transaction> transactions) {
    return null;
  }

  /**
   * Return true if all credit transactions are bellow the provided maximum amount
   */
  public boolean q3_areAllCreditTransactionBelow(BigDecimal maxAmount, Collection<Transaction> transactions) {
    return false;
  }

  /**
   * Returns the 3 highest credit transactions
   */
  public Collection<Transaction> q4_get3HighestCreditTransactions(Collection<Transaction> transactions) {
    return null;
  }

  /**
   * Return the new balance for an account by applying all new transactions to the current balance
   */
  public BigDecimal q5_calculateNewBalance(BigDecimal currentBalance, Collection<Transaction> newTransactions) {
    return null;
  }

  /**
   * Return the total amount broken down by categories
   */
  public Map<String, BigDecimal> q6_calculateByCategory(Collection<Transaction> stocks) {
    return null;
  }
}
