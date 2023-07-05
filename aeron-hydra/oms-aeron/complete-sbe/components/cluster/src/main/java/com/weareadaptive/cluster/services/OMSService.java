package com.weareadaptive.cluster.services;

import static com.weareadaptive.sbe.BufferUtils.E_CLEAR_Encoder;
import static com.weareadaptive.sbe.BufferUtils.E_RESET_Encoder;

import com.weareadaptive.cluster.clusterUtil.SessionMessageContext;
import com.weareadaptive.oms.Order;
import com.weareadaptive.oms.OrderbookImpl;
import com.weareadaptive.oms.util.ExecutionResult;
import com.weareadaptive.sbe.BufferUtils;
import com.weareadaptive.sbe.EncodeResult;

import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.aeron.cluster.service.ClientSession;

public class OMSService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OMSService.class);
    private final OrderbookImpl orderbook;
    private final SessionMessageContext sessionContext;

    public OMSService(SessionMessageContext sessionContext)
    {
        orderbook = new OrderbookImpl();
        this.sessionContext = sessionContext;
    }

    /**
     * * Receive Ingress binary encoding and place order in Orderbook
     * - Decode buffer
     * - Perform business logic
     * - Encode a response
     * - Offer Egress back to cluster client
     */
    public void placeOrder(final ClientSession session, final long correlationId, final DirectBuffer buffer, final int offset)
    {
        final Order allocatedOrder = BufferUtils.I_PO_Decoder(buffer, offset);
        final ExecutionResult result = orderbook.placeOrder(allocatedOrder.getPrice(), allocatedOrder.getSize(), allocatedOrder.getSide());
        LOGGER.info("Ingress-" + correlationId + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());
        final EncodeResult encodeResult = BufferUtils.E_PO_Encoder(correlationId, result.getOrderId(), result.getStatus());
        sessionContext.reply(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength());
    }

    /**
     * * Receive Ingress binary encoding and cancel order in Orderbook
     * - Decode buffer
     * - Perform business logic
     * - Encode a response
     * - Offer Egress back to cluster client
     */
    public void cancelOrder(final ClientSession session, long correlationId, DirectBuffer buffer, int offset)
    {
        final long orderId = BufferUtils.I_CO_Decoder(buffer, offset);
        final ExecutionResult result = orderbook.cancelOrder(orderId);
        LOGGER.info("Ingress-" + correlationId + " | OrderID: " + result.getOrderId() + " Status: " + result.getStatus());
        final EncodeResult encodeResult = BufferUtils.E_CO_Encoder(correlationId, result.getOrderId(), result.getStatus());
        sessionContext.reply(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength());
    }

    /**
     * * Receive Ingress binary encoding and clear Orderbook
     * - Decode buffer
     * - Perform business logic
     * - Encode a response
     * - Offer Egress back to cluster client
     */
    public void clearOrderbook(final ClientSession session, final long correlationId, final DirectBuffer buffer, final int offset)
    {
        orderbook.clear();
        LOGGER.info("Ingress-" + correlationId + " | Cleared Orderbook");
        final EncodeResult encodeResult = E_CLEAR_Encoder(correlationId);
        sessionContext.reply(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength());
    }

    /**
     * * Receive Ingress binary encoding and reset Orderbook
     * - Decode buffer
     * - Perform business logic
     * - Encode a response
     * - Offer Egress back to cluster client
     */
    public void resetOrderbook(ClientSession session, long correlationId, DirectBuffer buffer, int offset)
    {
        orderbook.reset();
        LOGGER.info("Ingress-" + correlationId + " | Reset Orderbook");
        final EncodeResult encodeResult = E_RESET_Encoder(correlationId);
        sessionContext.reply(encodeResult.getBuffer(), 0, encodeResult.getEncodedLength());
    }

    public OrderbookImpl getOrderbook()
    {
        return this.orderbook;
    }
}
