package com.adaptive.generics.advanced;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class WildcardWithSuperExample {

  @Test
  public void example() {
    var catShelter = new Shelter<Cat>();
    catShelter.add(new Cat("Cat1"));
    catShelter.add(new Cat("Cat2"));
    catShelter.add(new Cat("Cat3"));

    var dogShelter = new Shelter<Dog>();
    dogShelter.add(new Dog("Dog1"));
    dogShelter.add(new Dog("Dog2"));

    var anyPetShelter = new Shelter<Pet>();
    dogShelter.moveTo(anyPetShelter);
    catShelter.moveTo(anyPetShelter);
    assertEquals(5, anyPetShelter.getPets().size());
  }

  public static class Shelter<T> {
    private List<T> pets = new ArrayList<>();

    public void add(T pet) {
      pets.add(pet);
    }

    public List<T> getPets() {
      return Collections.unmodifiableList(pets);
    }

    public void moveTo(Shelter<? super T> shelter) {
      shelter.pets.addAll(pets);
    }
  }

  public static abstract class Pet {
    private final String name;

    public Pet(String name) {
      this.name = name;
    }

    public abstract int getNumberOfLegs();
  }

  public static class Dog extends Pet {
    public Dog(String name) {
      super(name);
    }

    public void bark() {
      System.out.println("BARK!!");
    }

    @Override
    public int getNumberOfLegs() {
      return 4;
    }
  }

  public static class Cat extends Pet {
    public Cat(String name) {
      super(name);
    }

    public void miow() {
      System.out.println("miow!!");
    }

    @Override
    public int getNumberOfLegs() {
      return 4;
    }
  }
}
