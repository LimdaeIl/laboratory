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

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
    ErrorCode errorCode = e.getErrorCode();
    log.warn("예외 발생: [{}] {}", errorCode.getCode(), errorCode.getMessage());
    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(new ErrorResponse(errorCode));
  }

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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnhandledException(Exception ex) {
    log.error("내부 예외 발생: {}", ex.getMessage(), ex);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(CommonErrorCode.INTERNAL_ERROR));
  }

  // (선택) JSON 파싱 실패 등 추가 핸들링
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException e) {
    log.warn("JSON 파싱 실패: {}", e.getMessage());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(CommonErrorCode.INVALID_INPUT));
  }
}
