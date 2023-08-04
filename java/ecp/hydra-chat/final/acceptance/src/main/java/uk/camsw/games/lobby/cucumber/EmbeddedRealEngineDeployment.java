package uk.camsw.games.lobby.cucumber;

import com.weareadaptive.chatroom.engine.EngineMain;
import com.weareadaptive.chatroom.engine.components.testing.cucumber.EngineEmbeddedRealDeployment;
import com.weareadaptive.chatroom.receiver.components.RecordingReceiverCliConnection;
import com.weareadaptive.chatroom.receiver.components.testing.cucumber.ReceiverCliEmbeddedRealDeployment;
import com.weareadaptive.chatroom.sender.components.RecordingSenderCliConnection;
import com.weareadaptive.chatroom.sender.components.testing.cucumber.SenderCliEmbeddedRealDeployment;
import com.weareadaptive.hydra.platform.engine.testing.ClusterDriver;
import org.agrona.CloseHelper;

public class EmbeddedRealEngineDeployment implements Deployment {

    private final EngineEmbeddedRealDeployment engineDeployment;
    private final SenderCliEmbeddedRealDeployment senderDeployment;
    private final ReceiverCliEmbeddedRealDeployment receiverDeployment;

    public EmbeddedRealEngineDeployment(final EngineEmbeddedRealDeployment engineDeployment,
                                        SenderCliEmbeddedRealDeployment senderDeployment,
                                        ReceiverCliEmbeddedRealDeployment receiverDeployment) {
        this.engineDeployment = engineDeployment;
        this.senderDeployment = senderDeployment;
        this.receiverDeployment = receiverDeployment;
    }


    @Override
    public ClusterDriver getEngine() {
        return engineDeployment.getEngine("engine", EngineMain.BOOTSTRAPPER);
    }

    @Override
    public RecordingSenderCliConnection getSenderClient() {
        return senderDeployment.getSenderCli("sender-client");
    }

    @Override
    public RecordingReceiverCliConnection getReceiverClient() {
        return receiverDeployment.getReceiverCli("receiver-client");
    }

    @Override
    public void close() {
        CloseHelper.closeAll(senderDeployment, receiverDeployment);
        CloseHelper.close(engineDeployment);
    }

}
