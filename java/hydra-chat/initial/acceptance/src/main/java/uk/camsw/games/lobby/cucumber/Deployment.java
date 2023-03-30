package uk.camsw.games.lobby.cucumber;

import com.weareadaptive.chatroom.receiver.components.RecordingReceiverCliConnection;
import com.weareadaptive.chatroom.sender.components.RecordingSenderCliConnection;
import com.weareadaptive.hydra.cucumber.deployment.HydraPlatformDeployment;
import com.weareadaptive.hydra.platform.engine.testing.ClusterDriver;

public interface Deployment extends HydraPlatformDeployment {

    ClusterDriver getEngine();

    RecordingSenderCliConnection getSenderClient();

    RecordingReceiverCliConnection getReceiverClient();
}
