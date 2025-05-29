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

  public ErrorResponse(ErrorCode errorCode) {
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
  }

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