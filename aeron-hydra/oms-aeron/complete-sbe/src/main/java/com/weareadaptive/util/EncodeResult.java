package com.weareadaptive.util;

import org.agrona.ExpandableArrayBuffer;

public class EncodeResult
{
    private final ExpandableArrayBuffer buffer;
    private final int encodedLength;

    public EncodeResult(final ExpandableArrayBuffer buffer, final int encodedLength)
    {
        this.buffer = buffer;
        this.encodedLength = encodedLength;
    }

    public ExpandableArrayBuffer getBuffer()
    {
        return buffer;
    }

    public int getEncodedLength()
    {
        return encodedLength;
    }
}
