package com.weareadaptive.katas.solutions;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
    import com.weareadaptive.katas.Streams;
    import java.util.Comparator;

public class StreamsSolution extends Streams {

  @Override
  public boolean q1_containsCategory(String category, Collection<Transaction> transactions) {
    return transactions.stream()
        .anyMatch(t -> t.category().equals(category));
  }

  @Override
  public Collection<String> q2_listCategories(Collection<Transaction> transactions) {
    return transactions.stream()
        .map(Transaction::category)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean q3_isAllCreditTransactionBellow(BigDecimal max,
                                                 Collection<Transaction> transactions) {
    return transactions.stream()
        .filter(t -> Direction.Credit == t.direction())
        .allMatch(t -> max.compareTo(t.amount()) > 0);
  }

  @Override
  public Collection<Transaction> q4_get3HighestCreditTransaction(Collection<Transaction> transaction) {
    return transaction.stream()
        .filter(t -> t.direction() == Direction.Credit)
        .sorted(Comparator.comparing(Transaction::amount).reversed())
        .limit(3)
        .toList();
  }

  @Override
  public BigDecimal q5_calculateNewBalance(BigDecimal currentBalance,
                                           Collection<Transaction> transactions) {
    return transactions.stream()
        .map(t -> t.amount().multiply(BigDecimal.valueOf(t.direction().multiplier())))
        .reduce(currentBalance, BigDecimal::add);
  }

  @Override
  public Map<String, BigDecimal> q6_calculateByCategory(Collection<Transaction> transaction) {
    return transaction.stream()
        .collect(Collectors.groupingBy(Transaction::category, Collectors.reducing(BigDecimal.ZERO, this::calculateRealAmount, BigDecimal::add)));
  }

  private BigDecimal calculateRealAmount(Transaction transaction) {
    return transaction.amount().multiply(BigDecimal.valueOf(transaction.direction().multiplier()));
  }
}
