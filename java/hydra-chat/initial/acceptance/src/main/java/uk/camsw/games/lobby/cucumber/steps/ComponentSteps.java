package uk.camsw.games.lobby.cucumber.steps;

import io.cucumber.java.en.Given;
import uk.camsw.games.lobby.cucumber.Deployment;

import java.util.List;
import java.util.stream.Stream;

public class ComponentSteps {
    private final Deployment deployment;

    public ComponentSteps(final Deployment deployment) {
        this.deployment = deployment;
    }

    @Given("the components {string}")
    public void startComponents(final String componentNames) {
        final List<String> services =
            Stream.of(componentNames.split(",")).map(String::trim).toList();

        services.forEach(componentName ->
        {
            switch (componentName) {
                case "engine" -> deployment.getEngine();
                case "receiver" -> deployment.getReceiverClient();
                case "sender" -> deployment.getSenderClient();
                default -> throw new IllegalArgumentException(
                    "Unknown component '" + componentName + "'");
            }
        });
    }
}
