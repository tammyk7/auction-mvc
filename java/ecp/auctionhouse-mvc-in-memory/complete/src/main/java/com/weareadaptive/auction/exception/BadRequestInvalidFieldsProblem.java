package com.weareadaptive.auction.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;

public class BadRequestInvalidFieldsProblem extends Problem {
  public final List<InvalidField> invalidFields;

  @JsonCreator
  public BadRequestInvalidFieldsProblem(List<InvalidField> invalidFields) {
    super(BAD_REQUEST.value(), BAD_REQUEST.getReasonPhrase(), BAD_REQUEST.getReasonPhrase());
    this.invalidFields = invalidFields;
  }
}
