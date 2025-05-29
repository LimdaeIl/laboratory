package com.book.laboratory.common.exception;


import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;

  /**
   * Constructs a new CustomException with the specified ErrorCode.
   *
   * The exception message is set to the message provided by the given ErrorCode.
   *
   * @param errorCode the ErrorCode associated with this exception
   */
  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  /**
   * Creates a new {@code CustomException} instance with the specified {@code ErrorCode}.
   *
   * @param errorCode the error code to associate with the exception
   * @return a new {@code CustomException} containing the provided error code
   */
  public static CustomException from(ErrorCode errorCode) {
    return new CustomException(errorCode);
  }

}