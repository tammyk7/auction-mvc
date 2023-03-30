package uk.camsw.games.lobby.cucumber.steps;

import com.weareadaptive.chatroom.allocated.AllocatedChatRoomEvent;
import com.weareadaptive.chatroom.allocated.AllocatedMessage;
import com.weareadaptive.chatroom.allocated.AllocatedMessageReceived;
import com.weareadaptive.chatroom.entities.ChatRoomEvent;
import com.weareadaptive.chatroom.entities.Message;
import com.weareadaptive.chatroom.entities.MutableMessage;
import com.weareadaptive.chatroom.services.ChatRoomServiceProxy;
import com.weareadaptive.hydra.cucumber.Expector;
import com.weareadaptive.hydra.platform.core.testing.StreamRecorderAdapter;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uk.camsw.games.lobby.cucumber.Deployment;

import java.util.List;
import java.util.Map;

public class ChatRoomSteps {

    private final Deployment deployment;
    private final Expector expector;

    public ChatRoomSteps(final Deployment deployment, final Expector expector) {
        this.deployment = deployment;
        this.expector = expector;
    }

    @When("{string} sends a message containing text {string}")
    public void sendMessage(final String user, final String text) {
        ChatRoomServiceProxy proxy = deployment.getSenderClient().services().channelToCluster().getChatRoomServiceProxy();
        try (var request = proxy.acquireBroadcastChatMessageRequest()) {
            MutableMessage message = request.message();
            message.user(user);
            message.text(text);
            proxy.broadcastMessage(proxy.allocateCorrelationId(), request);
        }
    }

    @DataTableType
    public Message message(final Map<String, String> row) {
        String user = row.get("user");
        String text = row.get("text");
        var message = new AllocatedMessage();
        message.user(user);
        message.text(text);
        return message;
    }

    @Then("the chatroom contains the messages")
    public void chatroomContains(final List<Message> messages) {
        StreamRecorderAdapter<ChatRoomEvent> events =
            deployment.getReceiverClient().services().channelToCluster().getChatRoomServiceClientRecorder().chatRoomEvent();
        var expectedEvents = messages.stream().map(msg ->
        {
            AllocatedChatRoomEvent event = new AllocatedChatRoomEvent();
            AllocatedMessageReceived received = event.messageReceived();
            received.message().user(msg.user());
            received.message().text(msg.text());
            return event;
        }).toArray(ChatRoomEvent[]::new);

        expector.expect(events)
            .withoutAnyCorrelation()
            .toContainExactly(expectedEvents);
    }
}
