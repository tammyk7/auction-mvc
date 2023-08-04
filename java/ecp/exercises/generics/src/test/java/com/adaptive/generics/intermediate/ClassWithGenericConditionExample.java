package com.adaptive.generics.intermediate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ClassWithGenericConditionExample {
  @Test
  public void example() {
    var numberAdder = new NumberAdder<Integer>();
    numberAdder.add(1);
    numberAdder.add(3);
    numberAdder.add(5);

    var average = numberAdder.average();

    assertEquals(3L, average);
  }

  public static class NumberAdder<T extends Number> {
    private final List<T> numbers = new ArrayList<>();

    public void add(T number) {
      numbers.add(number);
    }

    public long sum() {
      var sum = 0L;

      for (var number: numbers) {
        sum += number.longValue();
      }
      return sum;
    }

    public long average() {
      if (numbers.size() == 0) {
        return 0;
      }
      return sum() / numbers.size();
    }
  }
}
