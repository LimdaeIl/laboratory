package com.book.laboratory.common.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
  INVALID_INPUT(-1, "잘못된 입력 값입니다.", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED(-1, "인가 받지 않았습니다. 권한이 필요합니다.", HttpStatus.UNAUTHORIZED),
  FORBIDDEN(-1, "해당 리소스에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN),
  NOT_FOUND(-1, "요청하신 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  INTERNAL_ERROR(-1, "내부 서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  // Redis
  FAILED_SET_REDIS(-1, "레디스 저장을 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  FAILED_GET_REDIS(-1, "레디스 조회를 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);



  private final Integer code;
  private final String message;
  private final HttpStatus httpStatus;
}
