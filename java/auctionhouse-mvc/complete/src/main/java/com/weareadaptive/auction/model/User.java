package com.weareadaptive.auction.model;

import static org.apache.logging.log4j.util.Strings.isBlank;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity(name = "AuctionUser")
public class User {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private int id;
  private String username;
  private String password;
  private boolean isAdmin;
  private String firstName;
  private String lastName;
  private String organisation;
  private boolean isBlocked;

  public User(
      String username,
      String password,
      String firstName,
      String lastName,
      String organisation,
      boolean isAdmin) {
    if (isBlank(username)) {
      throw new BusinessException("username cannot be null or empty");
    }
    if (isBlank(password)) {
      throw new BusinessException("password cannot be null or empty");
    }
    if (isBlank(firstName)) {
      throw new BusinessException("firstName cannot be null or empty");
    }
    if (isBlank(lastName)) {
      throw new BusinessException("lastName cannot be null or empty");
    }
    if (isBlank(organisation)) {
      throw new BusinessException("organisation cannot be null or empty");
    }
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.organisation = organisation;
    this.isAdmin = isAdmin;
  }

  public User() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean validatePassword(String password) {
    return this.password.equals(password);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getId() {
    return id;
  }

  public String getOrganisation() {
    return organisation;
  }

  public void setOrganisation(String organisation) {
    this.organisation = organisation;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isBlocked() {
    return isBlocked;
  }

  public void setBlocked(boolean blocked) {
    isBlocked = blocked;
  }
}
