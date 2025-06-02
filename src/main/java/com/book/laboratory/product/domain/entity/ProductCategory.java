package com.book.laboratory.product.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductCategory {
  TOP("상의"),
  BOTTOM("하의"),
  SHOES("신발"),
  HAT("모자"),
  ACCESSOR0Y("악세사리");

  private final String description;
}
