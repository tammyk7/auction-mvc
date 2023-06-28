package com.weareadaptive.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.TreeSet;

import com.weareadaptive.cluster.services.oms.Order;
import com.weareadaptive.cluster.services.oms.OrderbookImpl;
import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.cluster.services.oms.util.Status;
import com.weareadaptive.sbe.CancelOrderEgressDecoder;
import com.weareadaptive.sbe.CancelOrderEgressEncoder;
import com.weareadaptive.sbe.CancelOrderIngressDecoder;
import com.weareadaptive.sbe.CancelOrderIngressEncoder;
import com.weareadaptive.sbe.ClearOrderbookEgressDecoder;
import com.weareadaptive.sbe.ClearOrderbookEgressEncoder;
import com.weareadaptive.sbe.ClearOrderbookIngressDecoder;
import com.weareadaptive.sbe.ClearOrderbookIngressEncoder;
import com.weareadaptive.sbe.MessageHeaderDecoder;
import com.weareadaptive.sbe.MessageHeaderEncoder;
import com.weareadaptive.sbe.OrderEgressDecoder;
import com.weareadaptive.sbe.OrderEgressEncoder;
import com.weareadaptive.sbe.OrderIngressDecoder;
import com.weareadaptive.sbe.OrderIngressEncoder;
import com.weareadaptive.sbe.ResetOrderbookEgressDecoder;
import com.weareadaptive.sbe.ResetOrderbookEgressEncoder;
import com.weareadaptive.sbe.ResetOrderbookIngressDecoder;
import com.weareadaptive.sbe.ResetOrderbookIngressEncoder;

import org.agrona.BitUtil;
import org.agrona.DirectBuffer;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

/**
 * Buffer Encoding Decoding Utility
 */
public class BufferOffsets
{
    /**
     * Start of all messages
     */
    public static final int COMMAND_OFFSET = 0;
    public static final int CORRELATION_OFFSET = COMMAND_OFFSET + Byte.BYTES;

    /**
     * PlaceOrder Ingress
     */
    public static final int I_PO_PRICE_OFFSET = CORRELATION_OFFSET + Long.BYTES;
    public static final int I_PO_SIZE_OFFSET = I_PO_PRICE_OFFSET + Double.BYTES;
    public static final int I_PO_SIDE_OFFSET = I_PO_SIZE_OFFSET + Long.BYTES;

    /**
     * PlaceOrder Egress
     */
    public static final int E_PO_ORDERID_OFFSET = CORRELATION_OFFSET + Long.BYTES;
    public static final int E_PO_STATUS_OFFSET = E_PO_ORDERID_OFFSET + Long.BYTES;

    /**
     * CancelOrder Ingress
     */
    public static final int I_CO_ORDERID_OFFSET = CORRELATION_OFFSET + Long.BYTES;

    /**
     * CancelOrder Egress
     */
    public static final int E_CO_ORDERID_OFFSET = CORRELATION_OFFSET + Long.BYTES;
    public static final int E_CO_STATUS_OFFSET = E_CO_ORDERID_OFFSET + Long.BYTES;

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
    static final ClearOrderbookIngressDecoder clearOrderbookIngressDecoder = new ClearOrderbookIngressDecoder();
    static final ClearOrderbookEgressEncoder clearOrderbookEgressEncoder = new ClearOrderbookEgressEncoder();
    static final ClearOrderbookEgressDecoder clearOrderbookEgressDecoder = new ClearOrderbookEgressDecoder();

    static final ResetOrderbookIngressEncoder resetOrderbookIngressEncoder = new ResetOrderbookIngressEncoder();
    static final ResetOrderbookIngressDecoder resetOrderbookIngressDecoder = new ResetOrderbookIngressDecoder();
    static final ResetOrderbookEgressEncoder resetOrderbookEgressEncoder = new ResetOrderbookEgressEncoder();
    static final ResetOrderbookEgressDecoder resetOrderbookEgressDecoder = new ResetOrderbookEgressDecoder();


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
        orderIngressEncoder.bidAsk((byte)(side == Side.BID ? 1 : 2));
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
        final Side side = orderIngressDecoder.bidAsk() == 1 ? Side.BID : Side.ASK;
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

    public static UnsafeBuffer SNAPSHOT_ENCODER(
        final OrderbookImpl ob,
        final HashMap<String,
            byte[]> orderbookByteArrays
    )
    {
        final int SNAPSHOT_ORDERS_ID_OFFSET_VAL = (BitUtil.SIZE_OF_INT * 4) + BitUtil.SIZE_OF_LONG;
        final int SNAPSHOT_ORDERS_OFFSET_VAL = SNAPSHOT_ORDERS_ID_OFFSET_VAL + BitUtil.SIZE_OF_LONG;
        final int SNAPSHOT_BIDS_ID_OFFSET_VAL = SNAPSHOT_ORDERS_OFFSET_VAL + orderbookByteArrays.get("orders").length;
        final int SNAPSHOT_ASKS_ID_OFFSET_VAL = SNAPSHOT_BIDS_ID_OFFSET_VAL + orderbookByteArrays.get("bids").length;
        final int SNAPSHOT_MSG_LENGTH = SNAPSHOT_ASKS_ID_OFFSET_VAL + orderbookByteArrays.get("asks").length;

        final UnsafeBuffer snapshotBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(SNAPSHOT_MSG_LENGTH));

        snapshotBuffer.putInt(0, SNAPSHOT_ORDERS_ID_OFFSET_VAL);
        snapshotBuffer.putInt(BitUtil.SIZE_OF_INT, SNAPSHOT_ORDERS_OFFSET_VAL);
        snapshotBuffer.putInt(BitUtil.SIZE_OF_INT * 2, SNAPSHOT_BIDS_ID_OFFSET_VAL);
        snapshotBuffer.putInt(BitUtil.SIZE_OF_INT * 3, SNAPSHOT_ASKS_ID_OFFSET_VAL);
        snapshotBuffer.putLong(BitUtil.SIZE_OF_INT * 4, SNAPSHOT_MSG_LENGTH);

        snapshotBuffer.putLong(SNAPSHOT_ORDERS_ID_OFFSET_VAL, ob.getOrderId());
        snapshotBuffer.putBytes(SNAPSHOT_ORDERS_OFFSET_VAL, orderbookByteArrays.get("orders"));
        snapshotBuffer.putBytes(SNAPSHOT_BIDS_ID_OFFSET_VAL, orderbookByteArrays.get("bids"));
        snapshotBuffer.putBytes(SNAPSHOT_ASKS_ID_OFFSET_VAL, orderbookByteArrays.get("asks"));
        return snapshotBuffer;
    }

    public static void SNAPSHOT_DECODER_LOAD_STATE(final OrderbookImpl ob, final MutableDirectBuffer totalSnapshotBuffer) throws IOException, ClassNotFoundException
    {
        final int SNAPSHOT_ORDERS_ID_OFFSET_VAL = totalSnapshotBuffer.getInt(0);
        final int SNAPSHOT_ORDERS_OFFSET_VAL = totalSnapshotBuffer.getInt(BitUtil.SIZE_OF_INT);
        final int SNAPSHOT_BIDS_OFFSET_VAL = totalSnapshotBuffer.getInt(BitUtil.SIZE_OF_INT * 2);
        final int SNAPSHOT_ASKS_OFFSET_VAL = totalSnapshotBuffer.getInt(BitUtil.SIZE_OF_INT * 3);
        final int SNAPSHOT_MSG_LENGTH = totalSnapshotBuffer.getInt(BitUtil.SIZE_OF_INT * 4);

        final long orderID = totalSnapshotBuffer.getLong(SNAPSHOT_ORDERS_ID_OFFSET_VAL);

        final byte[] ordersBytes = new byte[SNAPSHOT_BIDS_OFFSET_VAL - SNAPSHOT_ORDERS_OFFSET_VAL];
        totalSnapshotBuffer.getBytes(SNAPSHOT_ORDERS_OFFSET_VAL, ordersBytes);

        final byte[] bidsBytes = new byte[SNAPSHOT_ASKS_OFFSET_VAL - SNAPSHOT_BIDS_OFFSET_VAL];
        totalSnapshotBuffer.getBytes(SNAPSHOT_BIDS_OFFSET_VAL, bidsBytes);

        final byte[] asksBytes = new byte[SNAPSHOT_MSG_LENGTH - SNAPSHOT_ASKS_OFFSET_VAL];
        totalSnapshotBuffer.getBytes(SNAPSHOT_ASKS_OFFSET_VAL, asksBytes);

        ob.loadState(
            orderID,
            convertByteArrayToHashMap(ordersBytes),
            convertByteArrayToTreeSet(bidsBytes),
            convertByteArrayToTreeSet(asksBytes)
        );
    }

    public static HashMap<Long, Order> convertByteArrayToHashMap(final byte[] bytes) throws IOException, ClassNotFoundException
    {
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(bais))
        {
            return (HashMap<Long, Order>)ois.readObject();
        }
    }

    public static TreeSet<Order> convertByteArrayToTreeSet(final byte[] bytes) throws IOException, ClassNotFoundException
    {
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try (ObjectInputStream ois = new ObjectInputStream(bais))
        {
            return (TreeSet<Order>)ois.readObject();
        }
    }

    public static byte[] convertToByteArray(final Object obj) throws IOException
    {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos))
        {
            oos.writeObject(obj);
            oos.flush();
        }
        return baos.toByteArray();
    }


}
