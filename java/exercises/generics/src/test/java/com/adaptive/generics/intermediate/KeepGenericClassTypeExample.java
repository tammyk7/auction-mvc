package com.adaptive.generics.intermediate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class KeepGenericClassTypeExample {

  @Test
  public void example() {
    var classAppender = new ClassAppender<>(Integer.class);

    var value = classAppender.appendClass("test");

    assertEquals("Integer test", value);
  }

  public static class ClassAppender<T> {
    private final Class<T> tClass;

    public ClassAppender(Class<T> tClass) {
      this.tClass = tClass;
    }

    public String appendClass(String text) {
      return String.format("%s %s", tClass.getSimpleName(), text);
    }
  }
}

