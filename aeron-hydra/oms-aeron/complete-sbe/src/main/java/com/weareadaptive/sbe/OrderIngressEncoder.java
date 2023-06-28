/* Generated SBE (Simple Binary Encoding) message codec. */

package com.weareadaptive.sbe;

import org.agrona.MutableDirectBuffer;


/**
 * Ingress for a order placement
 */
@SuppressWarnings("all")
public final class OrderIngressEncoder
{
    public static final int BLOCK_LENGTH = 17;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 688;
    public static final int SCHEMA_VERSION = 1;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final OrderIngressEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    private int initialOffset;
    private int offset;
    private int limit;

    public static int priceId()
    {
        return 1;
    }

    public static int priceSinceVersion()
    {
        return 0;
    }

    public static int priceEncodingOffset()
    {
        return 0;
    }

    public static int priceEncodingLength()
    {
        return 8;
    }

    public static String priceMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static double priceNullValue()
    {
        return Double.NaN;
    }

    public static double priceMinValue()
    {
        return 4.9E-324d;
    }

    public static double priceMaxValue()
    {
        return 1.7976931348623157E308d;
    }

    public static int sizeId()
    {
        return 2;
    }

    public static int sizeSinceVersion()
    {
        return 0;
    }

    public static int sizeEncodingOffset()
    {
        return 8;
    }

    public static int sizeEncodingLength()
    {
        return 8;
    }

    public static String sizeMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long sizeNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long sizeMinValue()
    {
        return 0x0L;
    }

    public static long sizeMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public static int bidAskId()
    {
        return 3;
    }

    public static int bidAskSinceVersion()
    {
        return 0;
    }

    public static int bidAskEncodingOffset()
    {
        return 16;
    }

    public static int bidAskEncodingLength()
    {
        return 1;
    }

    public static String bidAskMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte bidAskNullValue()
    {
        return (byte)-128;
    }

    public static byte bidAskMinValue()
    {
        return (byte)-127;
    }

    public static byte bidAskMaxValue()
    {
        return (byte)127;
    }

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public MutableDirectBuffer buffer()
    {
        return buffer;
    }

    public int initialOffset()
    {
        return initialOffset;
    }

    public int offset()
    {
        return offset;
    }

    public OrderIngressEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);

        return this;
    }

    public OrderIngressEncoder wrapAndApplyHeader(
        final MutableDirectBuffer buffer, final int offset, final MessageHeaderEncoder headerEncoder)
    {
        headerEncoder
            .wrap(buffer, offset)
            .blockLength(BLOCK_LENGTH)
            .templateId(TEMPLATE_ID)
            .schemaId(SCHEMA_ID)
            .version(SCHEMA_VERSION);

        return wrap(buffer, offset + MessageHeaderEncoder.ENCODED_LENGTH);
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        this.limit = limit;
    }

    public OrderIngressEncoder price(final double value)
    {
        buffer.putDouble(offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public OrderIngressEncoder size(final long value)
    {
        buffer.putLong(offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public OrderIngressEncoder bidAsk(final byte value)
    {
        buffer.putByte(offset + 16, value);
        return this;
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        return appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final OrderIngressDecoder decoder = new OrderIngressDecoder();
        decoder.wrap(buffer, initialOffset, BLOCK_LENGTH, SCHEMA_VERSION);

        return decoder.appendTo(builder);
    }
}
