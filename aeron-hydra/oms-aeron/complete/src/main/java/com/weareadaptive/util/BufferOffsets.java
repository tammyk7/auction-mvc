package com.weareadaptive.util;

import com.weareadaptive.cluster.services.oms.Order;
import com.weareadaptive.cluster.services.oms.OrderbookImpl;
import com.weareadaptive.cluster.services.oms.util.ExecutionResult;
import com.weareadaptive.cluster.services.oms.util.Side;
import com.weareadaptive.cluster.services.oms.util.Status;
import org.agrona.BitUtil;
import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Buffer Encoding Decoding Utility
 */
public class BufferOffsets
{
    /**
     *  Start of all messages
     */
    public static final int COMMAND_OFFSET = 0;
    public static final int CORRELATION_OFFSET = COMMAND_OFFSET + Byte.BYTES;
    public static final int HEADER_LENGTH = CORRELATION_OFFSET + Long.BYTES;

    /**
     *  PlaceOrder Ingress
     */
    public static final int I_PO_PRICE_OFFSET = CORRELATION_OFFSET + Long.BYTES;
    public static final int I_PO_SIZE_OFFSET = I_PO_PRICE_OFFSET + Double.BYTES;
    public static final int I_PO_SIDE_OFFSET = I_PO_SIZE_OFFSET + Long.BYTES;
    public static final int I_PO_LENGTH = I_PO_SIDE_OFFSET + Byte.BYTES;

    /**
     *  PlaceOrder Egress
     */
    public static final int E_PO_ORDERID_OFFSET = CORRELATION_OFFSET + Long.BYTES;
    public static final int E_PO_STATUS_OFFSET = E_PO_ORDERID_OFFSET + Long.BYTES;
    public static final int E_PO_LENGTH = E_PO_STATUS_OFFSET + Byte.BYTES;

    /**
     *  CancelOrder Ingress
     */
    public static final int I_CO_ORDERID_OFFSET = CORRELATION_OFFSET + Long.BYTES;
    public static final int I_CO_LENGTH = I_CO_ORDERID_OFFSET + Long.BYTES;

    /**
     *  CancelOrder Egress
     */
    public static final int E_CO_ORDERID_OFFSET = CORRELATION_OFFSET + Long.BYTES;
    public static final int E_CO_STATUS_OFFSET = E_CO_ORDERID_OFFSET + Long.BYTES;
    public static final int E_CO_LENGTH = E_CO_STATUS_OFFSET + Byte.BYTES;

    /**
     *  Memory allocation
     */
    static final Order ALLOCATED_ORDER = new Order(-1, 0, 0, Side.BID);
    static final ExecutionResult ALLOCATED_EXECUTED_RESULT = new ExecutionResult(-1, null);

    public static UnsafeBuffer I_PO_ENCODER(
        final long correlation,
        final double price,
        final long size,
        final Side side
    )
    {
        final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(I_PO_LENGTH));
        buffer.putInt(COMMAND_OFFSET, 1);
        buffer.putLong(CORRELATION_OFFSET, correlation);
        buffer.putDouble(I_PO_PRICE_OFFSET, price);
        buffer.putLong(I_PO_SIZE_OFFSET, size);
        buffer.putByte(I_PO_SIDE_OFFSET, (byte)(side == Side.BID ? 1 : 2));
        return buffer;
    }

    public static Order I_PO_Decoder(
        final DirectBuffer buffer,
        final int offset
    )
    {
        final double price = buffer.getDouble(offset + I_PO_PRICE_OFFSET);
        final long size = buffer.getLong(offset + I_PO_SIZE_OFFSET);
        final Side side = buffer.getByte(offset + I_PO_SIDE_OFFSET) == 1 ? Side.BID : Side.ASK;
        ALLOCATED_ORDER.setPrice(price);
        ALLOCATED_ORDER.setSize(size);
        ALLOCATED_ORDER.setSide(side);
        return ALLOCATED_ORDER;
    }

    public static UnsafeBuffer E_PO_Encoder(
        final long correlation,
        final long orderId,
        final Status status
    )
    {
        final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(E_PO_LENGTH));
        buffer.putInt(COMMAND_OFFSET, 1);
        buffer.putLong(CORRELATION_OFFSET, correlation);
        buffer.putLong(E_PO_ORDERID_OFFSET, orderId);
        buffer.putByte(E_PO_STATUS_OFFSET, status.getValue());
        return buffer;
    }

    public static ExecutionResult E_PO_Decoder(
        final DirectBuffer buffer,
        final int offset
    )
    {
        final long orderId = buffer.getLong(offset + E_PO_ORDERID_OFFSET);
        final byte status = buffer.getByte(offset + E_PO_STATUS_OFFSET);
        ALLOCATED_EXECUTED_RESULT.setOrderId(orderId);
        ALLOCATED_EXECUTED_RESULT.setStatus(Status.fromByte(status));
        return ALLOCATED_EXECUTED_RESULT;
    }

    public static UnsafeBuffer I_CO_Encoder(
        final long correlation,
        final long orderId
    )
    {
        final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(I_CO_LENGTH));
        buffer.putInt(COMMAND_OFFSET, 2);
        buffer.putLong(CORRELATION_OFFSET, correlation);
        buffer.putLong(I_CO_ORDERID_OFFSET, orderId);
        return buffer;
    }

    public static long I_CO_Decoder(
        final DirectBuffer buffer,
        final int offset
    )
    {
        final long orderId = buffer.getLong(offset + I_CO_ORDERID_OFFSET);
        return orderId;
    }

    public static UnsafeBuffer E_CO_Encoder(
        final long correlation,
        final long orderId,
        final Status status
    )
    {
        final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(E_CO_LENGTH));
        buffer.putInt(COMMAND_OFFSET, 2);
        buffer.putLong(CORRELATION_OFFSET, correlation);
        buffer.putLong(E_CO_ORDERID_OFFSET, orderId);
        buffer.putByte(E_CO_STATUS_OFFSET, status.getValue());
        return buffer;
    }

    public static ExecutionResult E_CO_Decoder(
        final DirectBuffer buffer,
        final int offset
    )
    {
        final long orderId = buffer.getLong(offset + E_CO_ORDERID_OFFSET);
        final byte status = buffer.getByte(offset + E_CO_STATUS_OFFSET);
        ALLOCATED_EXECUTED_RESULT.setOrderId(orderId);
        ALLOCATED_EXECUTED_RESULT.setStatus(Status.fromByte(status));
        return ALLOCATED_EXECUTED_RESULT;
    }

    public static UnsafeBuffer I_CLEAR_Encoder(final long correlation)
    {
        final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(HEADER_LENGTH));
        buffer.putInt(COMMAND_OFFSET, 3);
        buffer.putLong(CORRELATION_OFFSET, correlation);
        return buffer;
    }

    public static UnsafeBuffer I_RESET_Encoder(final long correlation)
    {
        final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(HEADER_LENGTH));
        buffer.putInt(COMMAND_OFFSET, 4);
        buffer.putLong(CORRELATION_OFFSET, correlation);
        return buffer;
    }

    public static UnsafeBuffer E_CLEAR_Encoder(final long correlation)
    {
        final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(HEADER_LENGTH));
        buffer.putInt(COMMAND_OFFSET, 3);
        buffer.putLong(CORRELATION_OFFSET, correlation);
        return buffer;
    }

    public static UnsafeBuffer E_RESET_Encoder(final long correlation)
    {
        final UnsafeBuffer buffer = new UnsafeBuffer(ByteBuffer.allocateDirect(HEADER_LENGTH));
        buffer.putInt(COMMAND_OFFSET, 4);
        buffer.putLong(CORRELATION_OFFSET, correlation);
        return buffer;
    }

    public static UnsafeBuffer SNAPSHOT_ENCODER(
        final OrderbookImpl ob,
        final HashMap<String,
        byte[]> orderbookByteArrays
    )
    {
        final int SNAPSHOT_ORDERS_ID_OFFSET_VAL = (BitUtil.SIZE_OF_INT * 4) + BitUtil.SIZE_OF_LONG;
        final int SNAPSHOT_ORDERS_OFFSET_VAL = SNAPSHOT_ORDERS_ID_OFFSET_VAL + BitUtil.SIZE_OF_LONG;
        final int SNAPSHOT_BIDS_ID_OFFSET_VAL = SNAPSHOT_ORDERS_OFFSET_VAL +  orderbookByteArrays.get("orders").length;
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
