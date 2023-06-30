package com.weareadaptive.util;

import com.weareadaptive.cluster.services.oms.Order;
import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.cluster.services.oms.util.Status;
import com.weareadaptive.sbe.CancelOrderEgressDecoder;
import com.weareadaptive.sbe.CancelOrderEgressEncoder;
import com.weareadaptive.sbe.CancelOrderIngressDecoder;
import com.weareadaptive.sbe.CancelOrderIngressEncoder;
import com.weareadaptive.sbe.ClearOrderbookEgressEncoder;
import com.weareadaptive.sbe.ClearOrderbookIngressEncoder;
import com.weareadaptive.sbe.MessageHeaderDecoder;
import com.weareadaptive.sbe.MessageHeaderEncoder;
import com.weareadaptive.sbe.OrderEgressDecoder;
import com.weareadaptive.sbe.OrderEgressEncoder;
import com.weareadaptive.sbe.OrderIngressDecoder;
import com.weareadaptive.sbe.OrderIngressEncoder;
import com.weareadaptive.sbe.ResetOrderbookEgressEncoder;
import com.weareadaptive.sbe.ResetOrderbookIngressEncoder;

import org.agrona.DirectBuffer;
import org.agrona.ExpandableArrayBuffer;

/**
 * Buffer Encoding Decoding Utility
 */
public class BufferUtils
{
    /**
     * Memory allocation
     */
    static final Order ALLOCATED_ORDER = new Order(-1, 0, 0, Side.BID);
    static final ExecutionResult ALLOCATED_EXECUTED_RESULT = new ExecutionResult(-1, null);

    static final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    static final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    static final OrderIngressEncoder orderIngressEncoder = new OrderIngressEncoder();
    static final OrderIngressDecoder orderIngressDecoder = new OrderIngressDecoder();
    static final OrderEgressEncoder orderEgressEncoder = new OrderEgressEncoder();
    static final OrderEgressDecoder orderEgressDecoder = new OrderEgressDecoder();

    static final CancelOrderIngressEncoder cancelOrderIngressEncoder = new CancelOrderIngressEncoder();
    static final CancelOrderIngressDecoder cancelOrderIngressDecoder = new CancelOrderIngressDecoder();
    static final CancelOrderEgressEncoder cancelOrderEgressEncoder = new CancelOrderEgressEncoder();
    static final CancelOrderEgressDecoder cancelOrderEgressDecoder = new CancelOrderEgressDecoder();

    static final ClearOrderbookIngressEncoder clearOrderbookIngressEncoder = new ClearOrderbookIngressEncoder();
    static final ClearOrderbookEgressEncoder clearOrderbookEgressEncoder = new ClearOrderbookEgressEncoder();

    static final ResetOrderbookIngressEncoder resetOrderbookIngressEncoder = new ResetOrderbookIngressEncoder();
    static final ResetOrderbookEgressEncoder resetOrderbookEgressEncoder = new ResetOrderbookEgressEncoder();

    public static EncodeResult I_PO_ENCODER(
        final long correlation,
        final double price,
        final long size,
        final Side side
    )
    {
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(1024);
        orderIngressEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageHeaderEncoder.correlation(correlation);
        orderIngressEncoder.price(price);
        orderIngressEncoder.size(size);
        orderIngressEncoder.side((byte)(side == Side.BID ? 1 : 2));
        final int encodedLength = messageHeaderEncoder.encodedLength() + orderIngressEncoder.encodedLength();
        return new EncodeResult(buffer, encodedLength);
    }

    public static Order I_PO_Decoder(
        final DirectBuffer buffer,
        final int offset
    )
    {
        messageHeaderDecoder.wrap(buffer, offset);
        orderIngressDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        final double price = orderIngressDecoder.price();
        final long size = orderIngressDecoder.size();
        final Side side = orderIngressDecoder.side() == 1 ? Side.BID : Side.ASK;
        ALLOCATED_ORDER.setPrice(price);
        ALLOCATED_ORDER.setSize(size);
        ALLOCATED_ORDER.setSide(side);
        return ALLOCATED_ORDER;
    }

    public static EncodeResult E_PO_Encoder(
        final long correlation,
        final long orderId,
        final Status status
    )
    {
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(128);
        orderEgressEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageHeaderEncoder.correlation(correlation);
        orderEgressEncoder.orderid(orderId);
        orderEgressEncoder.status(status.getValue());
        final int encodedLength = messageHeaderEncoder.encodedLength() + orderEgressEncoder.encodedLength();
        return new EncodeResult(buffer, encodedLength);
    }

    public static ExecutionResult E_PO_Decoder(
        final DirectBuffer buffer,
        final int offset
    )
    {
        messageHeaderDecoder.wrap(buffer, offset);
        orderEgressDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        final long orderId = orderEgressDecoder.orderid();
        final byte status = orderEgressDecoder.status();
        ALLOCATED_EXECUTED_RESULT.setOrderId(orderId);
        ALLOCATED_EXECUTED_RESULT.setStatus(Status.fromByte(status));
        return ALLOCATED_EXECUTED_RESULT;
    }

    public static EncodeResult I_CO_Encoder(
        final long correlation,
        final long orderId
    )
    {
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(128);
        cancelOrderIngressEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageHeaderEncoder.correlation(correlation);
        cancelOrderIngressEncoder.orderid(orderId);
        final int encodedLength = messageHeaderEncoder.encodedLength() + cancelOrderIngressEncoder.encodedLength();
        return new EncodeResult(buffer, encodedLength);
    }

    public static long I_CO_Decoder(
        final DirectBuffer buffer,
        final int offset
    )
    {
        messageHeaderDecoder.wrap(buffer, offset);
        cancelOrderIngressDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        final long orderId = cancelOrderIngressDecoder.orderid();
        return orderId;
    }

    public static EncodeResult E_CO_Encoder(
        final long correlation,
        final long orderId,
        final Status status
    )
    {
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(128);
        cancelOrderEgressEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageHeaderEncoder.correlation(correlation);
        cancelOrderEgressEncoder.orderid(orderId);
        cancelOrderEgressEncoder.status(status.getValue());
        final int encodedLength = messageHeaderEncoder.encodedLength() + cancelOrderEgressEncoder.encodedLength();
        return new EncodeResult(buffer, encodedLength);
    }

    public static ExecutionResult E_CO_Decoder(
        final DirectBuffer buffer,
        final int offset
    )
    {
        messageHeaderDecoder.wrap(buffer, offset);
        cancelOrderEgressDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        final long orderId = cancelOrderEgressDecoder.orderid();
        final byte status = cancelOrderEgressDecoder.status();
        ALLOCATED_EXECUTED_RESULT.setOrderId(orderId);
        ALLOCATED_EXECUTED_RESULT.setStatus(Status.fromByte(status));
        return ALLOCATED_EXECUTED_RESULT;
    }

    public static EncodeResult I_CLEAR_Encoder(final long correlation)
    {
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(128);
        clearOrderbookIngressEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageHeaderEncoder.correlation(correlation);
        final int encodedLength = messageHeaderEncoder.encodedLength() + cancelOrderEgressEncoder.encodedLength();
        return new EncodeResult(buffer, encodedLength);
    }

    public static EncodeResult I_RESET_Encoder(final long correlation)
    {
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(128);
        resetOrderbookIngressEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageHeaderEncoder.correlation(correlation);
        final int encodedLength = messageHeaderEncoder.encodedLength() + resetOrderbookIngressEncoder.encodedLength();
        return new EncodeResult(buffer, encodedLength);
    }

    public static EncodeResult E_CLEAR_Encoder(final long correlation)
    {
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(128);
        clearOrderbookEgressEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageHeaderEncoder.correlation(correlation);
        final int encodedLength = messageHeaderEncoder.encodedLength() + clearOrderbookEgressEncoder.encodedLength();
        return new EncodeResult(buffer, encodedLength);
    }

    public static EncodeResult E_RESET_Encoder(final long correlation)
    {
        final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(128);
        resetOrderbookEgressEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        messageHeaderEncoder.correlation(correlation);
        final int encodedLength = messageHeaderEncoder.encodedLength() + resetOrderbookEgressEncoder.encodedLength();
        return new EncodeResult(buffer, encodedLength);
    }
}
