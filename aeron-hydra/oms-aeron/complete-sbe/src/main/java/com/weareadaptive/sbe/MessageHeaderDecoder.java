/* Generated SBE (Simple Binary Encoding) message codec. */

package com.weareadaptive.sbe;

import org.agrona.DirectBuffer;


/**
 * Message identifiers and length of message root
 */
@SuppressWarnings("all")
public final class MessageHeaderDecoder
{
    public static final int SCHEMA_ID = 688;
    public static final int SCHEMA_VERSION = 1;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final int ENCODED_LENGTH = 16;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private DirectBuffer buffer;

    public static int blockLengthEncodingOffset()
    {
        return 0;
    }

    public static int blockLengthEncodingLength()
    {
        return 2;
    }

    public static int blockLengthSinceVersion()
    {
        return 0;
    }

    public static int blockLengthNullValue()
    {
        return 65535;
    }

    public static int blockLengthMinValue()
    {
        return 0;
    }

    public static int blockLengthMaxValue()
    {
        return 65534;
    }

    public static int templateIdEncodingOffset()
    {
        return 2;
    }

    public static int templateIdEncodingLength()
    {
        return 2;
    }

    public static int templateIdSinceVersion()
    {
        return 0;
    }

    public static int templateIdNullValue()
    {
        return 65535;
    }

    public static int templateIdMinValue()
    {
        return 0;
    }

    public static int templateIdMaxValue()
    {
        return 65534;
    }

    public static int correlationEncodingOffset()
    {
        return 4;
    }

    public static int correlationEncodingLength()
    {
        return 8;
    }

    public static int correlationSinceVersion()
    {
        return 0;
    }

    public static long correlationNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long correlationMinValue()
    {
        return 0x0L;
    }

    public static long correlationMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public static int schemaIdEncodingOffset()
    {
        return 12;
    }

    public static int schemaIdEncodingLength()
    {
        return 2;
    }

    public static int schemaIdSinceVersion()
    {
        return 0;
    }

    public static int schemaIdNullValue()
    {
        return 65535;
    }

    public static int schemaIdMinValue()
    {
        return 0;
    }

    public static int schemaIdMaxValue()
    {
        return 65534;
    }

    public static int versionEncodingOffset()
    {
        return 14;
    }

    public static int versionEncodingLength()
    {
        return 2;
    }

    public static int versionSinceVersion()
    {
        return 0;
    }

    public static int versionNullValue()
    {
        return 65535;
    }

    public static int versionMinValue()
    {
        return 0;
    }

    public static int versionMaxValue()
    {
        return 65534;
    }

    public MessageHeaderDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;

        return this;
    }

    public DirectBuffer buffer()
    {
        return buffer;
    }

    public int offset()
    {
        return offset;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public int blockLength()
    {
        return (buffer.getShort(offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
    }

    public int templateId()
    {
        return (buffer.getShort(offset + 2, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
    }

    public long correlation()
    {
        return buffer.getLong(offset + 4, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

    public int schemaId()
    {
        return (buffer.getShort(offset + 12, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
    }

    public int version()
    {
        return (buffer.getShort(offset + 14, java.nio.ByteOrder.LITTLE_ENDIAN) & 0xFFFF);
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

        builder.append('(');
        builder.append("blockLength=");
        builder.append(this.blockLength());
        builder.append('|');
        builder.append("templateId=");
        builder.append(this.templateId());
        builder.append('|');
        builder.append("correlation=");
        builder.append(this.correlation());
        builder.append('|');
        builder.append("schemaId=");
        builder.append(this.schemaId());
        builder.append('|');
        builder.append("version=");
        builder.append(this.version());
        builder.append(')');

        return builder;
    }
}
