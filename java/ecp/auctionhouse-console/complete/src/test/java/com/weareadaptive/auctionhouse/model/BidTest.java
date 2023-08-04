package com.weareadaptive.auctionhouse.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.weareadaptive.auctionhouse.TestData;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BidTest {

  @Test
  @DisplayName("Should throw when the quantity is not above zero")
  public void shouldThrowWhenQuantityIsNotAboveZero() {
    //Act
    var exception = assertThrows(BusinessException.class,
        () -> new Bid(TestData.USER1, -1, 7.3, Instant.now()));

    //Assert
    assertTrue(exception.getMessage().contains("quantity"));
  }

  @Test
  @DisplayName("Should throw when the price is not above zero")
  public void shouldThrowWhenPriceIsNotAboveZero() {
    //Act
    var exception = assertThrows(BusinessException.class,
        () -> new Bid(TestData.USER1, 1, -7.3, Instant.now()));

    //Assert
    assertTrue(exception.getMessage().contains("price"));
  }

  @Test
  @DisplayName("Should throw when the user is null")
  public void shouldThrowWhenUserIsNull() {
    //Act
    var exception =
        assertThrows(BusinessException.class, () -> new Bid(null, 1, 7.3, Instant.now()));

    //Assert
    assertTrue(exception.getMessage().contains("user"));
  }
}
