package com.book.laboratory.common.exception;


import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public static CustomException from(ErrorCode errorCode) {
    return new CustomException(errorCode);
  }

}
