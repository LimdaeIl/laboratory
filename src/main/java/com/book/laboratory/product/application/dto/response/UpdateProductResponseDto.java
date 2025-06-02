package com.book.laboratory.product.application.dto.response;

import com.book.laboratory.product.domain.entity.Product;
import com.book.laboratory.product.domain.entity.ProductCategory;
import com.book.laboratory.product.domain.entity.ProductStatus;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UpdateProductResponseDto(
    UUID id,
    String name,
    Integer price,
    Integer quantity,
    String thumbnail,
    String description,
    ProductStatus status,
    ProductCategory category
) {

  public static UpdateProductResponseDto from(Product product) {
    return UpdateProductResponseDto.builder()
        .id(product.getId())
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
