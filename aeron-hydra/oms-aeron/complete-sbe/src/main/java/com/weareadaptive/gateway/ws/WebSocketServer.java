package com.weareadaptive.gateway.ws;

import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.gateway.clientLogic.ClientEgressListener;
import com.weareadaptive.gateway.clientLogic.ClientIngressSender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

/**
 * Gateway Websocket Server
 */
public class WebSocketServer extends AbstractVerticle
{

    private final ClientIngressSender clientIngressSender;
    private final ClientEgressListener clientEgressListener;
    private long correlation = 1;

    /**
     * Initialise with Ingress Sender and Egress Listener
     *
     * @param clientIngressSender  - Sending Ingress Messages to Cluster
     * @param clientEgressListener - Listening Egress Messages to Cluster
     */
    public WebSocketServer(
        final ClientIngressSender clientIngressSender,
        final ClientEgressListener clientEgressListener
    )
    {
        this.clientIngressSender = clientIngressSender;
        this.clientEgressListener = clientEgressListener;
    }

    @Override
    public void start()
    {
        vertx.createHttpServer()
            .webSocketHandler(this::wsHandler)
            .listen(8080);
    }

    /**
     * * Handle incoming websocket requests and implement routing logic
     * - Routing to appropriate logic should be handled via JSON command value:
     * - e.g: JSON payload to route to place order
     * {
     * "command": "place"
     * "payload":
     * {
     * "price": 10.00,
     * "size": 15,
     * "side": "BID"
     * }
     * }
     *
     * @param ws - websocket
     */
    private void wsHandler(final ServerWebSocket ws)
    {
        ws.handler(buffer ->
        {
            final JsonObject json = buffer.toJsonObject();
            final String command = json.getString("command");
            final JsonObject payload = json.getJsonObject("payload");
            clientEgressListener.addWSSession(correlation, ws);
            switch (command)
            {
                case "place" -> wsPlaceOrder(payload);
                case "cancel" -> wsCancelOrder(payload);
                case "clear" -> wsClearOrderbook();
                case "reset" -> wsResetOrderbook();
            }
            correlation++;
        });

        ws.closeHandler(v ->
        {
            clientEgressListener.removeWSSessions(ws);
        });
    }

    /**
     * * Handle Place Order request into Cluster, return status response to client
     * - e.g: JSON payload request
     * {
     * "command": "place"
     * "payload":
     * {
     * "price": 10.00,
     * "size": 15,
     * "side": "BID"
     * }
     * }
     * - e.g: JSON response (Use WS handler in ClientEgressListener)
     * {
     * "orderId": 1
     * "status": "FILLED"
     * }
     *
     * @param payload - JSON payload
     */
    private void wsPlaceOrder(final JsonObject payload)
    {
        final Double price = payload.getDouble("price");
        final long size = payload.getLong("size");
        final Side side = payload.getString("side").equals("BID") ? Side.BID : Side.ASK;
        clientIngressSender.placeOrder(correlation, price, size, side);
    }

    /**
     * * Handle request into Cluster, return ExecutionResult response to client
     * - e.g: JSON payload request
     * {
     * "command": "cancel"
     * "payload":
     * {
     * "orderId": 1
     * }
     * }
     * - e.g: JSON response (Use WS handler in ClientEgressListener)
     * {
     * "orderId": 1
     * "status": "CANCELLED"
     * }
     *
     * @param payload - JSON payload
     */
    private void wsCancelOrder(final JsonObject payload)
    {
        final long orderId = payload.getLong("orderId");
        clientIngressSender.cancelOrder(correlation, orderId);
    }

    /**
     * * Handle request into Cluster, return status response to client
     * - e.g: JSON response (Use WS handler in ClientEgressListener)
     * {
     * "status": "SUCCESS"
     * }
     */
    private void wsClearOrderbook()
    {
        clientIngressSender.clearOrderbook(correlation);
    }

    /**
     * * Handle request into Cluster, return status response to client
     * - e.g: JSON response (Use WS handler in ClientEgressListener)
     * {
     * "status": "SUCCESS"
     * }
     */
    private void wsResetOrderbook()
    {
        clientIngressSender.resetOrderbook(correlation);
    }
}
