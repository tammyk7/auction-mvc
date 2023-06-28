/* Generated SBE (Simple Binary Encoding) message codec. */

package com.weareadaptive.sbe;

import org.agrona.MutableDirectBuffer;


/**
 * Message identifiers and length of message root
 */
@SuppressWarnings("all")
public final class MessageHeaderEncoder
{
    public static final int SCHEMA_ID = 688;
    public static final int SCHEMA_VERSION = 1;
    public static final String SEMANTIC_VERSION = "0.1";
    public static final int ENCODED_LENGTH = 16;
    public static final java.nio.ByteOrder BYTE_ORDER = java.nio.ByteOrder.LITTLE_ENDIAN;

    private int offset;
    private MutableDirectBuffer buffer;

    public static int blockLengthEncodingOffset()
    {
        return 0;
    }

    public static int blockLengthEncodingLength()
    {
        return 2;
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

    public MessageHeaderEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        if (buffer != this.buffer)
        {
            this.buffer = buffer;
        }
        this.offset = offset;

        return this;
    }

    public MutableDirectBuffer buffer()
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

    public MessageHeaderEncoder blockLength(final int value)
    {
        buffer.putShort(offset + 0, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public MessageHeaderEncoder templateId(final int value)
    {
        buffer.putShort(offset + 2, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public MessageHeaderEncoder correlation(final long value)
    {
        buffer.putLong(offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public MessageHeaderEncoder schemaId(final int value)
    {
        buffer.putShort(offset + 12, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public MessageHeaderEncoder version(final int value)
    {
        buffer.putShort(offset + 14, (short)value, java.nio.ByteOrder.LITTLE_ENDIAN);
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

        final MessageHeaderDecoder decoder = new MessageHeaderDecoder();
        decoder.wrap(buffer, offset);

        return decoder.appendTo(builder);
    }
}
