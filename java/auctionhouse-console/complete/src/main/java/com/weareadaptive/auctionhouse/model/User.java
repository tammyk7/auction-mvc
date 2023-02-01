package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.StringUtil.isNullOrEmpty;

public class User implements Model {
  private final int id;
  private final String username;
  private final String password;
  private final String firstName;
  private final String lastName;
  private final String organisation;
  private final boolean isAdmin;

  public User(int id, String username, String password, String firstName, String lastName,
              String organisation) {
    this(id, username, password, firstName, lastName, organisation, false);
  }

  public User(int id, String username, String password, String firstName, String lastName,
              String organisation, boolean isAdmin) {
    if (isNullOrEmpty(username)) {
      throw new BusinessException("username cannot be null or empty");
    }
    if (isNullOrEmpty(password)) {
      throw new BusinessException("password cannot be null or empty");
    }
    if (isNullOrEmpty(firstName)) {
      throw new BusinessException("firstName cannot be null or empty");
    }
    if (isNullOrEmpty(lastName)) {
      throw new BusinessException("lastName cannot be null or empty");
    }
    if (isNullOrEmpty(organisation)) {
      throw new BusinessException("organisation cannot be null or empty");
    }
    this.id = id;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.organisation = organisation;
    this.isAdmin = isAdmin;
  }

  @Override
  public String toString() {
    return "User{"
        + "username='" + username + '\''
        + '}';
  }

  public String getUsername() {
    return username;
  }

  public boolean validatePassword(String password) {
    return this.password.equals(password);
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getId() {
    return id;
  }

  public String getOrganisation() {
    return organisation;
  }

  public boolean isAdmin() {
    return isAdmin;
  }
}
