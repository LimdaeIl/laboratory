package com.book.laboratory.product.application.dto.response;

import com.book.laboratory.product.domain.entity.Product;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record DeleteProductResponseDto(
    UUID id,
    String name,
    LocalDateTime deletedAt,
    Long deletedBy
) {
  public static DeleteProductResponseDto from(Product product) {
    return DeleteProductResponseDto.builder()
        .id(product.getId())
        .name(product.getName())
        .deletedAt(product.getDeletedAt())
        .deletedBy(product.getDeletedBy())
        .build();
  }
}
