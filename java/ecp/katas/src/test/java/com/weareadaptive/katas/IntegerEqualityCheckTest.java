package com.weareadaptive.katas;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

public class IntegerEqualityCheckTest {

  //IntegerEqualityCheck integerEqualityCheck = new com.weareadaptive.katas.solutions.IntegerEqualityCheckSolution();
  IntegerEqualityCheck integerEqualityCheck = new IntegerEqualityCheck();


  @Test
  public void shouldReturnWhetherOrNotIntegersAreEqual() {
    assertThat(integerEqualityCheck.areEqual(0, 0)).isTrue();
    assertThat(integerEqualityCheck.areEqual(1, 1)).isTrue();
    assertThat(integerEqualityCheck.areEqual(-1, -1)).isTrue();
    assertThat(integerEqualityCheck.areEqual(1, -1)).isFalse();
    assertThat(integerEqualityCheck.areEqual(127, 127)).isTrue();
    assertThat(integerEqualityCheck.areEqual(10, 100)).isFalse();
    assertThat(integerEqualityCheck.areEqual(null, 10)).isFalse();
    assertThat(integerEqualityCheck.areEqual(10, null)).isFalse();
    assertThat(integerEqualityCheck.areEqual(null, null)).isTrue();
  }

  @Test
  public void shouldReturnWhetherOrNotBiggerIntegersAreEqual() {
    assertThat(integerEqualityCheck.areEqual(9999999, 9999999)).isTrue();
    assertThat(integerEqualityCheck.areEqual(-9999999, -9999999)).isTrue();
    assertThat(integerEqualityCheck.areEqual(9999999, -9999999)).isFalse();
  }
}
