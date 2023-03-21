package com.weareadaptive.katas.solutions;

import com.weareadaptive.katas.IntegerEqualityCheck;

public class IntegerEqualityCheckSolution extends IntegerEqualityCheck {
  /**
   * Should return true when a and b have the same value.
   * <p>
   * For example:
   * a = 12, b = 12 should return true
   * a = 100, b = 99 should return false
   */
  public boolean areEqual(final Integer a, final Integer b) {
    //return Objects.equals(a,b);
    return a == null ? b == null : a.equals(b);
  }
}