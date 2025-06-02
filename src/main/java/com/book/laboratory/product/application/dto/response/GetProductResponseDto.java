package com.book.laboratory.product.application.dto.response;

import com.book.laboratory.product.domain.entity.Product;
import com.book.laboratory.product.domain.entity.ProductCategory;
import com.book.laboratory.product.domain.entity.ProductStatus;
import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserRole;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GetProductResponseDto(
    String name,
    Integer price,
    Integer quantity,
    String thumbnail,
    String description,
    ProductStatus status,
    ProductCategory category,
    LocalDateTime createdAt,
    Owner owner
) {

  public static GetProductResponseDto from(Product product, User user) {
    return GetProductResponseDto.builder()
        .name(product.getName())
        .price(product.getPrice())
        .quantity(product.getQuantity())
        .thumbnail(product.getThumbnail())
        .description(product.getDescription())
        .status(product.getStatus())
        .category(product.getCategory())
        .createdAt(product.getCreatedAt())
        .owner(Owner.builder()
            .name(user.getName())
            .email(user.getEmail())
            .profileImageUrl(user.getProfileImageUrl())
            .userRole(user.getUserRole())
            .build())
        .build();
  }

  @Builder(access = AccessLevel.PRIVATE)
  record Owner(
      String name,
      String email,
      String profileImageUrl,
      UserRole userRole
  ) {

  }
}
