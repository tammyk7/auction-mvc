package com.weareadaptive.auction.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException() {

  }

  public NotFoundException(String entity, int id) {
    super(String.format("%s (%s) was not found", entity, id));
  }
}
