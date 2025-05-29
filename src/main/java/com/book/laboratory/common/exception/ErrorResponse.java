package com.book.laboratory.common.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private final int code;
  private final String message;
  private List<FieldError> fieldErrors;

  /**
   * Constructs an ErrorResponse with the specified error code and message from the given ErrorCode.
   *
   * @param errorCode the ErrorCode containing the error code and message
   */
  public ErrorResponse(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
  }

  /**
   * Constructs an ErrorResponse with the specified error code, message, and a list of field-specific errors.
   *
   * @param errorCode the error code and message to use for this response
   * @param fieldErrors the list of field errors providing details about individual invalid fields
   */
  public ErrorResponse(ErrorCode errorCode, List<FieldError> fieldErrors) {
    this(errorCode.getCode(), errorCode.getMessage(), fieldErrors);
  }

  @Getter
  @AllArgsConstructor
  public static class FieldError {
    private String field;
    private String value;
    private String reason;
  }
}