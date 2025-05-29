package com.book.laboratory.user.domain;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.book.laboratory.common.response.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum UserSuccessCode implements SuccessCode {

  // ────────────── [회원 조회 관련] ──────────────
  USER_GET_SUCCESS(0, "회원 조회에 성공했습니다.", OK),
  USERS_GET_SUCCESS(0, "회원 목록 조회에 성공했습니다.", OK),

  // ────────────── [회원 수정/삭제 관련] ──────────────
  USER_UPDATE_SUCCESS(0, "회원 정보 수정에 성공했습니다.", OK),
  USER_EMAIL_UPDATE_SUCCESS(0, "회원 이메일 수정에 성공했습니다.", OK),
  USER_PATCH_PASSWORD_SUCCESS(0, "회원 비밀번호 수정에 성공했습니다.", OK),
  USER_PATCH_ROLE_SUCCESS(0, "회원 권한 수정에 성공했습니다.", OK),
  USER_DELETE_BY_ID_SUCCESS(0, "회원 삭제에 성공했습니다.", OK),

  // ────────────── [회원가입/로그인 관련] ──────────────
  USER_SIGNUP_SUCCESS(0, "회원가입이 성공적으로 완료되었습니다.", CREATED),
  USER_LOGIN_SUCCESS(0, "로그인이 성공적으로 완료되었습니다.", OK),
  USER_LOGOUT_SUCCESS(0, "로그아웃이 성공적으로 완료되었습니다.", OK),

  // ────────────── [이메일 인증 관련] ──────────────
  SEND_CODE_SUCCESS(0, "이메일 인증 코드 전송에 성공했습니다.", OK),
  VERIFY_CODE_SUCCESS(0, "이메일 인증 코드가 일치합니다.", OK),
  EMAIL_AVAILABLE(0, "사용 가능한 이메일입니다.", OK),
  EMAIL_DUPLICATE(0, "이미 사용 중인 이메일입니다.", OK),

  // ────────────── [휴대전화번호 관련] ──────────────
  PHONE_AVAILABLE(0, "사용 가능한 휴대전화번호입니다.", OK),
  PHONE_DUPLICATE(0, "이미 사용 중인 휴대전화번호입니다.", OK),

  // ────────────── [JWT 토큰 관련] ──────────────
  USER_TOKEN_GENERATION_SUCCESS(0, "토큰이 성공적으로 발급되었습니다.", OK),
  USER_TOKEN_VALIDATION_SUCCESS(0, "토큰이 유효합니다.", OK);

  private final Integer code;
  private final String message;
  private final HttpStatus httpStatus;
}