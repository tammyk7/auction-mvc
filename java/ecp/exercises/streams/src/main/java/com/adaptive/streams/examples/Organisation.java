package com.adaptive.streams.examples;

import java.util.List;

public record Organisation(String name, String countryCode, List<Employee> employees) {

}
