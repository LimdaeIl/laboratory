package com.book.laboratory.product.application.dto.response;

import com.book.laboratory.product.domain.entity.ProductCategory;

public record GetProductsResponseDto(
    String name,
    Integer price,
    ProductCategory category,
    String thumbnail
) {
}
