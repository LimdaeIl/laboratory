package com.book.laboratory.product.application.dto.response;

import com.book.laboratory.product.domain.entity.Product;
import com.book.laboratory.product.domain.entity.ProductCategory;
import com.book.laboratory.product.domain.entity.ProductStatus;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CreateProductResponseDto(
    String name,
    Integer price,
    Integer quantity,
    String thumbnail,
    String description,
    ProductStatus status,
    ProductCategory category
) {

  public static CreateProductResponseDto from(Product product) {
    return CreateProductResponseDto.builder()
        .name(product.getName())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .thumbnail(product.getThumbnail())
        .description(product.getDescription())
        .status(product.getStatus())
        .category(product.getCategory())
        .build();
  }

}
