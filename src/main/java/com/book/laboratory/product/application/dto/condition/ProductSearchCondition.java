package com.book.laboratory.product.application.dto.condition;

import com.book.laboratory.product.domain.entity.ProductCategory;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record ProductSearchCondition(
    String name,
    Integer priceFrom,
    Integer priceTo,
    ProductCategory category,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime createdFrom,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime createdTo
) {
}
