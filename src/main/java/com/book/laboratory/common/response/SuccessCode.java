package com.book.laboratory.common.response;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
  Integer getCode();
  String getMessage();
  HttpStatus getHttpStatus();
}
