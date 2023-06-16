# Adaptive websocket

## Getting Started for Adaptive staff

1. Run Adaptive onboarding
2. Clone this repo
3. Ensure the direnv/asdf integration is active and working. (use `direnv allow .` to activate it on the repository)
4. If not installed, install latest maven manually: https://maven.apache.org/install.html
   

## Build the project

```shell
mvn clean install
```

## Run the project locally

```shell
mvn exec:java
```

### Using Docker

To do.

## Committing

## Tech stack:
1. Java 17
2. Spring and Spring Boot
3. Web on reactive stack (Webflux) https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html
   - Websocket: https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-websocket
4. Project Reactor: https://projectreactor.io/
5. WebSocket client: https://github.com/TooTallNate/Java-WebSocket
   1. This web socket client is implementing RFC 7692 protocol

## Configuration:

See application.yaml and logback.xml (logging settings)
