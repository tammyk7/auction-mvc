package com.weareadaptive.auctionhouse.model;

import com.weareadaptive.auctionhouse.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
