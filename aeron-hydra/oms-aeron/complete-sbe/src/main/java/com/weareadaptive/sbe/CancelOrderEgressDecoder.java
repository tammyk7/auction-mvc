/* Generated SBE (Simple Binary Encoding) message codec. */

package com.weareadaptive.sbe;

import org.agrona.DirectBuffer;


/**
 * Egress for a cancel order placement
 */
@SuppressWarnings("all")
public final class CancelOrderEgressDecoder
{
    public static final int BLOCK_LENGTH = 9;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 688;
    public static final int SCHEMA_VERSION = 1;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private final CancelOrderEgressDecoder parentMessage = this;
    int actingBlockLength;
    int actingVersion;
    private DirectBuffer buffer;
    private int initialOffset;
    private int offset;
    private int limit;

    public static int orderidId()
    {
        return 1;
    }

    public static int orderidSinceVersion()
    {
        return 0;
    }

    public static int orderidEncodingOffset()
    {
        return 0;
    }

    public static int orderidEncodingLength()
    {
        return 8;
    }

    public static String orderidMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static long orderidNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long orderidMinValue()
    {
        return 0x0L;
    }

    public static long orderidMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public static int statusId()
    {
        return 2;
    }

    public static int statusSinceVersion()
    {
        return 0;
    }

    public static int statusEncodingOffset()
    {
        return 8;
    }

    public static int statusEncodingLength()
    {
        return 1;
    }

    public static String statusMetaAttribute(final MetaAttribute metaAttribute)
    {
        if (MetaAttribute.PRESENCE == metaAttribute)
        {
            return "required";
        }

        return "";
    }

    public static byte statusNullValue()
    {
        return (byte)-128;
    }

    public static byte statusMinValue()
    {
        return (byte)-127;
    }

    public static byte statusMaxValue()
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

    public CancelOrderEgressDecoder wrap(
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

    public CancelOrderEgressDecoder wrapAndApplyHeader(
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

    public CancelOrderEgressDecoder sbeRewind()
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

    public long orderid()
    {
        return buffer.getLong(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public byte status()
    {
        return buffer.getByte(offset + 8);
    }


    public String toString()
    {
        if (null == buffer)
        {
            return "";
        }

        final CancelOrderEgressDecoder decoder = new CancelOrderEgressDecoder();
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
        builder.append("[CancelOrderEgress](sbeTemplateId=");
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
        builder.append("orderid=");
        builder.append(this.orderid());
        builder.append('|');
        builder.append("status=");
        builder.append(this.status());

        limit(originalLimit);

        return builder;
    }

    public CancelOrderEgressDecoder sbeSkip()
    {
        sbeRewind();

        return this;
    }
}
