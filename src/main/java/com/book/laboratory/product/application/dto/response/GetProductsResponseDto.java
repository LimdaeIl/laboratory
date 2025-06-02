package com.book.laboratory.product.application.dto.response;

import com.book.laboratory.product.domain.entity.ProductCategory;
import java.util.UUID;

public record GetProductsResponseDto(
    UUID id,
    String name,
    Integer price,
    ProductCategory category,
    String thumbnail
) {
}
