package com.book.laboratory.product.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
  ACTIVE("활성화"),
  INACTIVE("비활성화"),
  SOLD_OUT("매진");

  private final String description;
}
