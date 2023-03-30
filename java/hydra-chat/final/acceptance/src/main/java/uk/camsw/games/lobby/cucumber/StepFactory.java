package uk.camsw.games.lobby.cucumber;

import com.weareadaptive.hydra.cucumber.TestMode;
import com.weareadaptive.hydra.cucumber.steps.HydraPlatformStepFactory;

public class StepFactory extends HydraPlatformStepFactory {

    @Override
    public void onStart(final TestMode testMode) {
        switch (testMode) {
            case EMBEDDED_DIRECT -> addClass(EmbeddedDirectEngineDeployment.class);
            case EMBEDDED_REAL -> addClass(EmbeddedRealEngineDeployment.class);
        }
    }
}
