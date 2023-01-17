package uk.camsw.games.lobby.cucumber;

import com.weareadaptive.chatroom.engine.EngineMain;
import com.weareadaptive.chatroom.engine.components.testing.cucumber.EngineEmbeddedDirectDeployment;
import com.weareadaptive.chatroom.receiver.components.RecordingReceiverCliConnection;
import com.weareadaptive.chatroom.receiver.components.testing.cucumber.ReceiverCliEmbeddedDirectDeployment;
import com.weareadaptive.chatroom.sender.components.RecordingSenderCliConnection;
import com.weareadaptive.chatroom.sender.components.testing.cucumber.SenderCliEmbeddedDirectDeployment;
import com.weareadaptive.hydra.platform.engine.testing.ClusterDriver;
import org.agrona.CloseHelper;

public class EmbeddedDirectEngineDeployment implements Deployment {
    private static final String CONFIG_RESOURCE_NAME = "functional-tests";
    private final EngineEmbeddedDirectDeployment engineDeployment;
    private final SenderCliEmbeddedDirectDeployment senderDeployment;
    private final ReceiverCliEmbeddedDirectDeployment receiverDeployment;

    public EmbeddedDirectEngineDeployment(
        final EngineEmbeddedDirectDeployment engineDeployment,
        final SenderCliEmbeddedDirectDeployment senderDeployment,
        final ReceiverCliEmbeddedDirectDeployment receiverDeployment
    ) {
        this.engineDeployment = engineDeployment;
        this.senderDeployment = senderDeployment;
        this.receiverDeployment = receiverDeployment;
    }

    @Override
    public ClusterDriver getEngine() {
        return engineDeployment.getEngine(CONFIG_RESOURCE_NAME, EngineMain.BOOTSTRAPPER);
    }

    @Override
    public RecordingSenderCliConnection getSenderClient() {
        return senderDeployment.getSenderCli(CONFIG_RESOURCE_NAME);
    }

    @Override
    public RecordingReceiverCliConnection getReceiverClient() {
        return receiverDeployment.getReceiverCli(CONFIG_RESOURCE_NAME);
    }


    @Override
    public void close() {
        CloseHelper.closeAll(senderDeployment, receiverDeployment);
        CloseHelper.close(engineDeployment);
    }
}
