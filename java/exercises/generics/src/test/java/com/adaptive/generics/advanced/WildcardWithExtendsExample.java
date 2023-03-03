package com.adaptive.generics.advanced;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

public class WildcardWithExtendsExample {

  @Test
  public void example() {
    var numbers = List.of(
      1, 3, 5
    );

    var average = average(numbers);

    assertEquals(3L, average);
  }

  public static long average(Iterable<? extends Number> numbers) {
    var sum = 0L;
    var count = 0;

    for (var current : numbers) {
      sum += current.longValue();
      count += 1;
    }
    return sum / count;
  }
}
