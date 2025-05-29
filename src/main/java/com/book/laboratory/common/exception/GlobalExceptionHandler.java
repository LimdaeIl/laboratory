package com.book.laboratory.common.exception;

import com.book.laboratory.common.exception.ErrorResponse.FieldError;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "GlobalExceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles CustomException by returning a structured error response with the appropriate HTTP status.
   *
   * @param e the CustomException containing the error code and details
   * @return a ResponseEntity with the error response and corresponding HTTP status
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    ErrorCode errorCode = e.getErrorCode();
    log.warn("예외 발생: [{}] {}", errorCode.getCode(), errorCode.getMessage());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(new ErrorResponse(errorCode));
  }

  /**
   * Handles validation errors for method arguments and returns a structured error response.
   *
   * Extracts field-level validation errors from the exception, maps them to a list of field error details, and responds with an error code indicating invalid input.
   *
   * @param e the exception containing validation errors for method arguments
   * @return a response entity with an error response detailing the invalid input and field errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
    List<FieldError> fieldErrors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> new FieldError(
            error.getField(),
            String.valueOf(error.getRejectedValue()),
            error.getDefaultMessage()
        ))
        .collect(Collectors.toList());

    log.warn("유효성 검사 실패: {}개 필드 에러 - {}", fieldErrors.size(), fieldErrors);
    return ResponseEntity
        .status(CommonErrorCode.INVALID_INPUT.getHttpStatus())
        .body(new ErrorResponse(CommonErrorCode.INVALID_INPUT, fieldErrors));
  }

  /**
   * Handles all uncaught exceptions and returns a standardized internal server error response.
   *
   * @return a ResponseEntity containing an ErrorResponse with HTTP status 500
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnhandledException(Exception ex) {
    log.error("내부 예외 발생: {}", ex.getMessage(), ex);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(CommonErrorCode.INTERNAL_ERROR));
  }

  /**
   * Handles cases where the request body cannot be parsed, such as invalid JSON input.
   *
   * Returns a 400 Bad Request response with an error code indicating invalid input.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException e) {
    log.warn("JSON 파싱 실패: {}", e.getMessage());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(CommonErrorCode.INVALID_INPUT));
  }
}
