package com.weareadaptive.gateway.ws;

import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.gateway.client.ClientEgressListener;
import com.weareadaptive.gateway.client.ClientIngressSender;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;

public class WebSocketServer extends AbstractVerticle
{

    ClientIngressSender clientIngressSender;
    ClientEgressListener clientEgressListener;

    public WebSocketServer(final ClientIngressSender clientIngressSender, final ClientEgressListener clientEgressListener) {
        this.clientIngressSender = clientIngressSender;
        this.clientEgressListener = clientEgressListener;
    }

    private long correlation = 1;

    @Override
    public void start()
    {
        vertx.createHttpServer()
                .webSocketHandler(this::WSHandler)
                .listen(8080);
    }

    /**
     * * Handle incoming websocket requests and implement routing logic
     *  - Routing to appropriate logic should be handled via JSON command value:
     *      - e.g: JSON payload to route to place order
     *      {
     *          "command": "place"
     *          "payload":
     *          {
     *              "price": 10.00,
     *              "size": 15,
     *              "side": "BID"
     *          }
     *      }
     */
    private void WSHandler(ServerWebSocket ws)
    {
        ws.handler(buffer ->
        {
            JsonObject json = buffer.toJsonObject();
            String command = json.getString("command");
            JsonObject payload = json.getJsonObject("payload");
            clientEgressListener.addWSSession(correlation,ws);
            switch (command) {
                case "place" -> WSPlaceOrder(payload);
                case "cancel" -> WSCancelOrder(payload);
                case "clear" -> WSClearOrderbook();
                case "reset" -> WSResetOrderbook();
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
     *
     *      - e.g: JSON payload request
     *      {
     *          "command": "place"
     *          "payload":
     *          {
     *              "price": 10.00,
     *              "size": 15,
     *              "side": "BID"
     *          }
     *      }
     *
     *      - e.g: JSON response (Use WS handler in ClientEgressListener)
     *      {
     *          "orderId": 1
     *          "status": "FILLED"
     *      }
     */
    private void WSPlaceOrder(JsonObject payload)
    {
            Double price = payload.getDouble("price");
            long size = payload.getLong("size");
            Side side = payload.getString("side").equals("BID") ? Side.BID : Side.ASK;
            clientIngressSender.PlaceOrder(correlation, price, size, side);
    }

    /**
     * * Handle request into Cluster, return ExecutionResult response to client
     *
     *      - e.g: JSON payload request
     *      {
     *          "command": "cancel"
     *          "payload":
     *          {
     *              "orderId": 1
     *          }
     *      }
     *
     *      - e.g: JSON response (Use WS handler in ClientEgressListener)
     *      {
     *          "orderId": 1
     *          "status": "CANCELLED"
     *      }
     */
    private void WSCancelOrder(JsonObject payload)
    {
            long orderId = payload.getLong("orderId");
            clientIngressSender.CancelOrder(correlation, orderId);
    }

    /**
     * * Handle request into Cluster, return status response to client
     *
     *      - e.g: JSON response (Use WS handler in ClientEgressListener)
     *      {
     *          "status": "SUCCESS"
     *      }
     */
    private void WSClearOrderbook()
    {
        clientIngressSender.ClearOrderbook(correlation);
    }

    /**
     * * Handle request into Cluster, return status response to client
     *
     *      - e.g: JSON response (Use WS handler in ClientEgressListener)
     *      {
     *          "status": "SUCCESS"
     *      }
     */
    private void WSResetOrderbook()
    {
        clientIngressSender.ResetOrderbook(correlation);
    }
}
