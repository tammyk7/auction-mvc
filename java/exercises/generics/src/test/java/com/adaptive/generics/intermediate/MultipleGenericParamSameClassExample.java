package com.adaptive.generics.intermediate;

import org.junit.jupiter.api.Test;

public class MultipleGenericParamSameClassExample {

  @Test
  public void example() {
    var tupple1 = Tupple2.create(3, "test");

    var tupple2 = Tupple3.create("dsafds", 88L, 3);
  }

  public static class Tupple2<T0, T1> {
    private final T0 t0;
    private final T1 t1;

    public Tupple2(T0 t0, T1 t1) {
      this.t0 = t0;
      this.t1 = t1;
    }

    public T0 getT0() {
      return t0;
    }

    public T1 getT1() {
      return t1;
    }

    public static <T0, T1> Tupple2<T0, T1> create(T0 t0, T1 t1) {
      return new Tupple2<>(t0, t1);
    }
  }

  public static class Tupple3<T0, T1, T2> {
    private final T0 t0;
    private final T1 t1;
    private final T2 t2;

    public Tupple3(T0 t0, T1 t1, T2 t2) {
      this.t0 = t0;
      this.t1 = t1;
      this.t2 = t2;
    }

    public T0 getT0() {
      return t0;
    }

    public T1 getT1() {
      return t1;
    }

    public T2 getT2() {
      return t2;
    }

    public static <T0, T1, T2> Tupple3<T0, T1, T2> create(T0 t0, T1 t1, T2 t2) {
      return new Tupple3<>(t0, t1, t2);
    }
  }
}
