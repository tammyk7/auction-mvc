package com.adaptive.generics.intermediate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;
import org.junit.jupiter.api.Test;

public class ClassWithMoreComplexGenericConditionExample {
  @Test
  public void example() {
    var maxkeeper = new MaximumKeeper<Integer>();
    maxkeeper.apply(45);
    maxkeeper.apply(4);
    maxkeeper.apply(7667);

    var max = maxkeeper.getMaximum();

    assertEquals(7667, max);
  }

  public static class MaximumKeeper<T extends Comparable<T>> {
    private T max;

    public T getMaximum() {
      return max;
    }

    public void apply(T value) {
      Objects.requireNonNull(value);

      if (max == null) {
        max = value;
      }
      if (max.compareTo(value) < 0) {
        max = value;
      }
    }
  }
}
