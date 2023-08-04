package com.adaptive.generics.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class OnMethodExample {

  @Test
  public void example() {
    List<String> a = List.of("Hahaha", "bad", "erere");
    List<String> b = List.of("Hahaha", "test1", "test2");

    List<String> union = union(a, b);

    assertEquals(1, union.size());
    assertEquals("Hahaha", union.get(0));
  }

  public static <T> List<T> union(List<T> a, List<T> b) {
    List<T> union = new ArrayList<>();
    for (var aValue : a) {
      if (b.contains(aValue)) {
        union.add(aValue);
      }
    }
    return union;
  }
}
