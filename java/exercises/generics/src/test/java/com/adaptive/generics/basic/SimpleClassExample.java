package com.adaptive.generics.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class SimpleClassExample {

  @Test
  public void example() {
    var maxKeeper = new MaximumKeeper<Integer>(Comparator.naturalOrder());
    maxKeeper.apply(7);
    maxKeeper.apply(877);
    maxKeeper.apply(8);

    var max = maxKeeper.getMaximum();

    assertEquals(877, max);
  }

  public static class MaximumKeeper<T> {
    private final Comparator<T> comparator;
    private T max;

    public MaximumKeeper(Comparator<T> comparator) {
      Objects.requireNonNull(comparator);

      this.comparator = comparator;
    }

    public T getMaximum() {
      return max;
    }

    public void apply(T value) {
      Objects.requireNonNull(value);

      if (max == null) {
        max = value;
      }
      if (comparator.compare(max, value) < 0) {
        max = value;
      }
    }
  }
}
