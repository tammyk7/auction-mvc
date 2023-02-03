package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.TestData.ADMIN;
import static com.weareadaptive.auctionhouse.TestData.ORG_1;
import static com.weareadaptive.auctionhouse.TestData.ORG_2;
import static com.weareadaptive.auctionhouse.TestData.USER1;
import static com.weareadaptive.auctionhouse.TestData.USER2;
import static com.weareadaptive.auctionhouse.TestData.USER3;
import static com.weareadaptive.auctionhouse.TestData.USER4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserStateTest {
  private UserState state;

  @BeforeEach
  public void initState() {
    state = new UserState();
    Stream.of(
        ADMIN,
        USER1,
        USER2,
        USER3,
        USER4
    ).forEach(u -> state.add(u));
    state.setNextId(USER4.getId());
  }




}
