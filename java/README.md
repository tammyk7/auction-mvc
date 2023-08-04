# Java Training Guide

## Tech Stack

We use the following back-end technologies at Adaptive, it may be worth looking into these if you don't know them:

- Java 17+
- Gradle
- Docker
- Vert.x (REST)
- Databases: Flyway and PostgresSQL
- Spring and Spring Boot (some projects)
  - Spring Framework
  - Spring Web MVC
  - Spring WebFlux / Project Reator
  - Spring Data JPA
- Hydra Platform (in-house, some projects)
  - Aeron transport, cluster and archive
  - RAFT consensus algorithm
  - FIX (Hydra Platform/Artio)

## Java Features

You should know the following modern Java features well at Adaptive:

- Records

  - https://www.baeldung.com/java-record-keyword
  - https://howtodoinjava.com/java/java-record-type/

- Lambda Expressions

  - https://howtodoinjava.com/java8/lambda-expressions/

- Streams

  - https://howtodoinjava.com/java/stream/java-streams-by-examples/

- Memory management

  - https://developers.redhat.com/articles/2021/08/20/stages-and-levels-java-garbage-collection
  - https://developers.redhat.com/articles/2021/09/09/how-jvm-uses-and-allocates-memory
  - https://developers.redhat.com/articles/2021/11/02/how-choose-best-java-garbage-collector

- Try-with-resources and AutoCloseable

  - https://www.baeldung.com/java-try-with-resources
  - https://howtodoinjava.com/java/try-with-resources/

- Flyweight pattern

  - https://www.baeldung.com/java-flyweight
  - https://refactoring.guru/design-patterns/flyweight
  - https://refactoring.guru/design-patterns/flyweight/java/example
