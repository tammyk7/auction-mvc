package com.weareadaptive.auctionhouse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class StringUtilTest {

  private static Stream<Arguments> testArguments() {
    return Stream.of(
        Arguments.of(null, true),
        Arguments.of("", true),
        Arguments.of("  ", true),
        Arguments.of("test", false)
    );
  }

  @ParameterizedTest(name = "{0} should return {1}")
  @MethodSource("testArguments")
  public void shouldTestStringIsNotNullOrBlank(String input, boolean expectedResult) {
    assertEquals(expectedResult, StringUtil.isNullOrEmpty(input));
  }
}
