package com.weareadaptive.oms.util;

public enum Status
{
    RESTING((byte)1),
    PARTIAL((byte)2),
    FILLED((byte)3),
    CANCELLED((byte)4),
    NONE((byte)5);

    private final byte value;

    Status(byte value)
    {
        this.value = value;
    }

    public static Status fromByte(byte value)
    {
        for (Status status : Status.values())
        {
            if (status.value == value)
            {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid byte value.");
    }

    public byte getValue()
    {
        return this.value;
    }
}
