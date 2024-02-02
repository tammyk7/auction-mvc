package com.weareadaptive.auction.model;

import com.weareadaptive.auction.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BidTest
{
    @Test
    @DisplayName("When the timestamp is null")
    public void timestampIsNull()
    {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> new Bid(100, 2.45, null, TestData.USER1));

        assertTrue(exception.getMessage().contains("timestamp"));
    }
}
