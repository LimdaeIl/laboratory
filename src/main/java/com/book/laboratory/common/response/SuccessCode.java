package com.book.laboratory.common.response;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
  /****
 * Returns the numeric code representing a successful operation.
 *
 * @return the success code as an Integer
 */
Integer getCode();
  /**
 * Returns the descriptive message associated with the success code.
 *
 * @return a human-readable success message
 */
String getMessage();
  /**
 * Returns the HTTP status associated with this success code.
 *
 * @return the corresponding HttpStatus
 */
HttpStatus getHttpStatus();
}
