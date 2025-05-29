package com.book.laboratory.user.domain;

import com.book.laboratory.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCode {

  // ────────────── [회원 조회 관련] ──────────────
  USER_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND.value(), "회원 ID를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  USER_NOT_FOUND_BY_PHONE(HttpStatus.NOT_FOUND.value(), "회원 휴대전화번호를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND.value(), "회원 이메일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  USER_GET_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "해당 회원 정보를 조회할 권한이 없습니다.", HttpStatus.FORBIDDEN),
  USER_ROLE_NOT_VALID(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 회원 권한입니다.", HttpStatus.BAD_REQUEST),

  // ────────────── [회원 가입 관련] ──────────────
  USER_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "이미 존재하는 회원입니다.", HttpStatus.CONFLICT),
  USER_ALREADY_SIGNED_UP(HttpStatus.CONFLICT.value(), "이미 가입된 회원입니다.", HttpStatus.CONFLICT),
  DUPLICATE_PHONE(HttpStatus.CONFLICT.value(), "휴대전화번호: 이미 존재하는 휴대전화번호입니다.", HttpStatus.CONFLICT),
  DUPLICATE_EMAIL(HttpStatus.CONFLICT.value(), "이메일: 이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
  USER_SIGNUP_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원가입에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

  // ────────────── [회원 정보 수정 관련] ──────────────
  USER_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원 정보 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  USER_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "회원 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_PATCH_EMAIL(HttpStatus.FORBIDDEN.value(), "자신의 이메일만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
  INVALID_PATCH_USER(HttpStatus.FORBIDDEN.value(), "자신의 정보만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
  INVALID_PATCH_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),

  // ────────────── [로그인 관련] ──────────────
  INVALID_LOGIN(HttpStatus.BAD_REQUEST.value(), "아이디 또는 비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST.value(), "비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

  // ────────────── [토큰 관련] ──────────────
  TOKEN_EXPIRED(HttpStatus.BAD_REQUEST.value(), "해당 토큰이 만료되었습니다.", HttpStatus.BAD_REQUEST),
  INVALID_BEARER_TOKEN(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
  REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "리프레시 토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  MALFORMED_TOKEN(HttpStatus.BAD_REQUEST.value(), "JWT 형식이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
  TAMPERED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "JWT 서명이 위조되었거나 무결성이 손상되었습니다.", HttpStatus.UNAUTHORIZED),
  TOKEN_TOO_EARLY(HttpStatus.BAD_REQUEST.value(), "JWT 활성화 시간이 아직 되지 않았습니다.", HttpStatus.BAD_REQUEST);

  private final Integer code;
  private final String message;
  private final HttpStatus httpStatus;
}