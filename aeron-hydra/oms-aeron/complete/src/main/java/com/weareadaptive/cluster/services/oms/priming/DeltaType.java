package com.weareadaptive.cluster.services.oms.priming;

public enum DeltaType
{
    ADD((byte) 1),
    REMOVE((byte) 2),
    RESIZE((byte) 3);

    private final byte value;

    DeltaType(byte value) {
        this.value = value;
    }

    public byte getValue()
    {
        return this.value;
    }

    public static DeltaType fromByte(byte value)
    {
        for (DeltaType delta : DeltaType.values())
        {
            if (delta.value == value)
            {
                return delta;
            }
        }
        throw new IllegalArgumentException("Invalid byte value.");
    }
}