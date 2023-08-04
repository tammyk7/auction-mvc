package com.adaptive.streams.examples;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.adaptive.streams.examples.Employee;
import com.adaptive.streams.examples.Organisation;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class BasicExample {
  private List<Organisation> organisations;

  public BasicExample() {
    organisations = of(
      new Organisation("Org 1", "CA", of(
          new Employee("JF", "Leg", LocalDate.of(1987, 8, 3)),
          new Employee("Alain", "Tremblay", LocalDate.of(1956, 7, 4))
        )),
      new Organisation("Org 2", "US", of(
        new Employee("Jeff", "Smith", LocalDate.of(1978, 8, 3))
        )),
      new Organisation("Org 3", "CA", of(
        new Employee("John", "Do", LocalDate.of(2001, 8, 3))
        )));
  }

  @Test
  public void simpleMap() {
    var organisationNames = organisations.stream()
      .map(Organisation::name)
      .toList();

    assertEquals(3, organisationNames.size());
    assertEquals("Org 1", organisationNames.get(0));
    assertEquals("Org 2", organisationNames.get(1));
    assertEquals("Org 3", organisationNames.get(2));
  }

  @Test
  public void flapMap() {
    var employeeNames = organisations.stream()
      .flatMap(o -> o.employees().stream())
      .map(e -> e.firstName() + " " + e.lastName())
      .toList();

    assertEquals(4, employeeNames.size());
    assertEquals("JF Leg", employeeNames.get(0));
    assertEquals("Alain Tremblay", employeeNames.get(1));
    assertEquals("Jeff Smith", employeeNames.get(2));
    assertEquals("John Do", employeeNames.get(3));
  }

  @Test
  public void distinct() {
    Collection<String> countries = organisations.stream()
      .map(Organisation::countryCode)
      .collect(Collectors.toSet());

    assertEquals(2, countries.size());
    assertTrue(countries.contains("CA"));
    assertTrue(countries.contains("US"));
  }

  @Test
  public void filter() {
    var usOrganisation = organisations.stream()
        .filter(o -> o.countryCode().equals("US"))
        .toList();

    assertEquals(1, usOrganisation.size());
    assertEquals("Org 2", usOrganisation.get(0).name());
  }

}
