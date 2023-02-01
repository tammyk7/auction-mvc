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

  @Test
  @DisplayName("findOrganisations should return all uniques organisations")
  public void shouldReturnAllUniqueOrganisations() {
    //Arrange

    //Act
    var result = state.findOrganisations();

    //Assert
    assertEquals(2, result.size());
    assertEquals(ORG_1, result.get(0));
    assertEquals(ORG_2, result.get(1));
  }

  @Test
  @DisplayName("findOrganisationsDetail should return the detail of organisations")
  public void shouldReturnOrganisationsDetail() {
    //Arrange

    //Act
    var result = state.getOrganisationsDetails();

    //Assert
    assertEquals(2, result.size());
    var org1 = result.get(0);
    assertEquals(ORG_1, org1.organisationName());
    assertEquals(2, org1.users().size());
    assertEquals(USER1.getUsername(), org1.users().get(0).getUsername());
    assertEquals(USER2.getUsername(), org1.users().get(1).getUsername());
    var org2 = result.get(1);
    assertEquals(ORG_2, org2.organisationName());
    assertEquals(2, org2.users().size());
    assertEquals(USER3.getUsername(), org2.users().get(0).getUsername());
    assertEquals(USER4.getUsername(), org2.users().get(1).getUsername());
  }

  @Test
  @DisplayName("add user should throw on duplicate user")
  public void shouldThrowWhenCreatingDuplicateUser() {
    //Arrange
    User duplicateUser = new User(
        state.nextId(),
        "testuser1",
        "password",
        "first",
        "name",
        "muorg");

    //Act
    assertThrows(BusinessException.class, () -> state.add(duplicateUser),
        UserState.ITEM_ALREADY_EXISTS);

  }

}
