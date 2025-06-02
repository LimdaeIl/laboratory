package com.book.laboratory.product.domain.entity;

import com.book.laboratory.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ProductErrorCode implements ErrorCode {

  // ────────────── [상품 조회 관련] ──────────────
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  PRODUCT_GET_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "상품 조회 권한이 없습니다.", HttpStatus.FORBIDDEN),

  // ────────────── [상품 등록 관련] ──────────────
  PRODUCT_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "상품 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  DUPLICATE_PRODUCT_NAME(HttpStatus.CONFLICT.value(), "이미 존재하는 상품 이름입니다.", HttpStatus.CONFLICT),

  // ────────────── [상품 수정 관련] ──────────────
  PRODUCT_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "상품 수정에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  PRODUCT_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "상품 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_PRODUCT_CATEGORY(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 상품 카테고리입니다.", HttpStatus.BAD_REQUEST),
  INVALID_PRODUCT_STATUS(HttpStatus.BAD_REQUEST.value(), "유효하지 않은 상품 상태입니다.", HttpStatus.BAD_REQUEST),
  INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST.value(), "상품 가격은 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
  INVALID_PRODUCT_QUANTITY(HttpStatus.BAD_REQUEST.value(), "상품 수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
  INVALID_PRODUCT_NAME(HttpStatus.BAD_REQUEST.value(), "상품 이름은 1 글자 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
  INVALID_PRODUCT_DESCRIPTION(HttpStatus.BAD_REQUEST.value(), "상품 내용은 1 글자 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
  PRODUCT_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "해당 상품 수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
  PRODUCT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "해당 상품 삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),


  // ────────────── [상품 상태 관련] ──────────────
  PRODUCT_ALREADY_INACTIVE(HttpStatus.BAD_REQUEST.value(), "이미 비활성화된 상품입니다.", HttpStatus.BAD_REQUEST),
  PRODUCT_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST.value(), "이미 활성화된 상품입니다.", HttpStatus.BAD_REQUEST),
  PRODUCT_OUT_OF_STOCK(HttpStatus.BAD_REQUEST.value(), "상품 재고가 부족합니다.", HttpStatus.BAD_REQUEST);

  private final Integer code;
  private final String message;
  private final HttpStatus httpStatus;
}