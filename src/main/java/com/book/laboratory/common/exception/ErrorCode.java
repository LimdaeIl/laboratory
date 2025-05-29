package com.book.laboratory.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  /****
 * Returns the numeric code representing the specific error.
 *
 * @return the error code as an Integer
 */
Integer getCode();
  /****
 * Returns the error message associated with this error code.
 *
 * @return a descriptive error message
 */
String getMessage();
  /****
 * Returns the HTTP status associated with this error code.
 *
 * @return the corresponding HttpStatus
 */
HttpStatus getHttpStatus();
}
