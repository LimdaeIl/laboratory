package com.book.laboratory.product.application.dto.requset;

import com.book.laboratory.product.domain.entity.ProductCategory;
import com.book.laboratory.product.domain.entity.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateProductRequestDto(
    @NotBlank(message = "상품: 상품 이름은 필수입니다.")
    @Size(max = 100, message = "상품: 상품 이름은 100자 이내여야 합니다.")
    String name,

    @NotNull(message = "상품가격: 가격은 필수입니다.")
    @Positive(message = "상품가격: 가격은 0보다 커야 합니다.")
    Integer price,

    @NotNull(message = "상품수량: 수량은 필수입니다.")
    @Min(value = 0, message = "상품수량: 수량은 0 이상이어야 합니다.")
    Integer quantity,

    @Size(max = 512, message = "썸네일: 썸네일 경로는 512자 이내여야 합니다.")
    String thumbnail,

    @NotBlank(message = "상품설명: 상품 설명은 필수입니다.")
    String description,

    @NotNull(message = "상품상태: 상품 상태는 필수입니다.")
    ProductStatus status,

    @NotNull(message = "상품카테고리: 상품 카테고리는 필수입니다.")
    ProductCategory category

) {
}
