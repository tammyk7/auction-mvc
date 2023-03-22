package com.weareadaptive.katas;

import static com.google.common.truth.Truth.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StreamsTest {

  //Streams streams = new com.weareadaptive.katas.solutions.StreamsSolution();
  Streams streams = new Streams();

  List<Streams.Transaction> transactions = getTransactions();

  private List<Streams.Transaction> getTransactions() {
    Instant now = Instant.now();

    var t1 = new Streams.Transaction(BigDecimal.valueOf(100), now.plus(2, ChronoUnit.MINUTES), Streams.Direction.Credit, "Gaz");
    var t2 = new Streams.Transaction(BigDecimal.valueOf(250), now.plus(45, ChronoUnit.MINUTES), Streams.Direction.Credit, "Restaurant");
    var t3 = new Streams.Transaction(BigDecimal.valueOf(70), now.plus(70, ChronoUnit.MINUTES), Streams.Direction.Credit, "Restaurant");
    var t4 = new Streams.Transaction(BigDecimal.valueOf(2000), now.plus(80, ChronoUnit.MINUTES), Streams.Direction.Debit, "Pay");
    var t5 = new Streams.Transaction(BigDecimal.valueOf(300), now.plus(200, ChronoUnit.MINUTES), Streams.Direction.Credit, "Grocery");
    var t6 = new Streams.Transaction(BigDecimal.valueOf(236), now.plus(300, ChronoUnit.MINUTES), Streams.Direction.Credit, "Grocery");
    var t7 = new Streams.Transaction(BigDecimal.valueOf(95), now.plus(350, ChronoUnit.MINUTES), Streams.Direction.Credit, "Gaz");

    return Arrays.asList(t1, t2, t3, t4, t5, t6, t7);
  }

  @Test
  public void test_q1_containsCategory() {
    assertThat(streams.q1_containsCategory("Gaz", transactions)).isTrue();
    assertThat(streams.q1_containsCategory("Bad", transactions)).isFalse();
  }

  @Test
  public void test_q2_listCategories() {
    assertThat(streams.q2_listCategories(transactions)).hasSize(4);
    assertThat(streams.q2_listCategories(transactions)).containsExactly("Gaz", "Restaurant", "Pay", "Grocery");
  }

  @Test
  public void test_q3_areAllCreditTransactionBelow() {
    assertThat(streams.q3_areAllCreditTransactionBelow(BigDecimal.valueOf(350), transactions)).isTrue();
    assertThat(streams.q3_areAllCreditTransactionBelow(BigDecimal.valueOf(260), transactions)).isFalse();
    assertThat(streams.q3_areAllCreditTransactionBelow(BigDecimal.valueOf(250), transactions)).isFalse();
  }

  @Test
  public void test_q4_get3HighestCreditTransactions() {
    var result = streams.q4_get3HighestCreditTransactions(transactions).stream().map(Streams.Transaction::amount).toList();
    assertThat(result).containsExactly(BigDecimal.valueOf(300), BigDecimal.valueOf(250), BigDecimal.valueOf(236));
  }

  @Test
  public void test_q5_calculateNewBalance() {
    assertThat(streams.q5_calculateNewBalance(BigDecimal.valueOf(100), transactions)).isEqualTo(BigDecimal.valueOf(1049));
  }

  @Test
  public void test_q6_calculateByCategory() {
    var result = streams.q6_calculateByCategory(transactions);
    assertThat(result.size()).isEqualTo(4);
    assertThat(result.get("Pay")).isEqualTo(BigDecimal.valueOf(2000));
    assertThat(result.get("Gaz")).isEqualTo(BigDecimal.valueOf(-195));
    assertThat(result.get("Restaurant")).isEqualTo(BigDecimal.valueOf(-320));
    assertThat(result.get("Grocery")).isEqualTo(BigDecimal.valueOf(-536));
  }

}
