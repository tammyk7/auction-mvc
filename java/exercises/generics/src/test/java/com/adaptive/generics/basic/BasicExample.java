package com.adaptive.generics.basic;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class BasicExample {

  @Test
  public void rawTypeProblem() {
    List theList = new ArrayList();

    // Unable to know what type it is
    // It can contains any types
    theList.add("dsfasdf");
    theList.add(89);

    // Need to cast to be able to use
    System.out.println("The length: " +((String)theList.get(0)).length());
  }

  @Test
  public void listWithGeneric() {
    List<String> theList = new ArrayList<>();

    // Can only insert String
    theList.add("Yo");
    theList.add("dsafsdafa");

    // Get value without casting
    String theString = theList.get(0);

    // But, ...
    // Generic does not exist in the ByteCode
    List openList = theList;
    openList.add(45);
    try {
      //Oups, the list is now invalid, it will fail to cast the Integer to an int
      String invalid = theList.get(1);
    } catch (ClassCastException exception) {

    }
  }

}
