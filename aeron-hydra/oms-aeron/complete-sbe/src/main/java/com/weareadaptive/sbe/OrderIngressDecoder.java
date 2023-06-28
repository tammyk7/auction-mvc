/* Generated SBE (Simple Binary Encoding) message codec. */

package com.weareadaptive.sbe;

import org.agrona.DirectBuffer;


/**
 * Ingress for a order placement
 */
@SuppressWarnings("all")
public final class OrderIngressDecoder
{
    public static final int BLOCK_LENGTH = 17;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 688;
    public static final int SCHEMA_VERSION = 1;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final OrderIngressDecoder parentMessage = this;
    int actingBlockLength;
    int actingVersion;
    private DirectBuffer buffer;
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

    public DirectBuffer buffer()
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

    public OrderIngressDecoder wrap(
        final DirectBuffer buffer,
        final int offset,
        final int actingBlockLength,
        final int actingVersion)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.initialOffset = offset;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

        return this;
    }

    public OrderIngressDecoder wrapAndApplyHeader(
        final DirectBuffer buffer,
        final int offset,
        final MessageHeaderDecoder headerDecoder)
    {
        headerDecoder.wrap(buffer, offset);

        final int templateId = headerDecoder.templateId();
        if (TEMPLATE_ID != templateId)
        {
            throw new IllegalStateException("Invalid TEMPLATE_ID: " + templateId);
        }

        return wrap(
            buffer,
            offset + MessageHeaderDecoder.ENCODED_LENGTH,
            headerDecoder.blockLength(),
            headerDecoder.version());
    }

    public OrderIngressDecoder sbeRewind()
    {
        return wrap(buffer, initialOffset, actingBlockLength, actingVersion);
    }

    public int sbeDecodedLength()
    {
        final int currentLimit = limit();
        sbeSkip();
        final int decodedLength = encodedLength();
        limit(currentLimit);

        return decodedLength;
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

    public double price()
    {
        return buffer.getDouble(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public long size()
    {
        return buffer.getLong(offset + 8, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public byte bidAsk()
    {
        return buffer.getByte(offset + 16);
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final OrderIngressDecoder decoder = new OrderIngressDecoder();
        decoder.wrap(buffer, initialOffset, actingBlockLength, actingVersion);

        return decoder.appendTo(new StringBuilder()).toString();
    }

    public StringBuilder appendTo(final StringBuilder builder)
    {
        if (null == buffer)
        {
            return builder;
        }

        final int originalLimit = limit();
        limit(initialOffset + actingBlockLength);
        builder.append("[OrderIngress](sbeTemplateId=");
        builder.append(TEMPLATE_ID);
        builder.append("|sbeSchemaId=");
        builder.append(SCHEMA_ID);
        builder.append("|sbeSchemaVersion=");
        if (parentMessage.actingVersion != SCHEMA_VERSION)
        {
            builder.append(parentMessage.actingVersion);
            builder.append('/');
        }
        builder.append(SCHEMA_VERSION);
        builder.append("|sbeBlockLength=");
        if (actingBlockLength != BLOCK_LENGTH)
        {
            builder.append(actingBlockLength);
            builder.append('/');
        }
        builder.append(BLOCK_LENGTH);
        builder.append("):");
        builder.append("price=");
        builder.append(this.price());
        builder.append('|');
        builder.append("size=");
        builder.append(this.size());
        builder.append('|');
        builder.append("bidAsk=");
        builder.append(this.bidAsk());

        limit(originalLimit);

        return builder;
    }

    public OrderIngressDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}
