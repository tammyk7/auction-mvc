package com.weareadaptive.cluster.services.oms.util;

public enum Side
{
    BID((byte)1),
    ASK((byte)2);

    private final byte value;

    Side(byte value)
    {
        this.value = value;
    }

    public static Side fromByte(byte value)
    {
        for (Side side : Side.values())
        {
            if (side.value == value)
            {
                return side;
            }
        }
        throw new IllegalArgumentException("Invalid byte value.");
    }

    public byte getValue()
    {
        return this.value;
    }
}