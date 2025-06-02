package com.book.laboratory.product.domain.repository;

import com.book.laboratory.product.application.dto.condition.ProductSearchCondition;
import com.book.laboratory.product.application.dto.response.GetProductsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {

  Page<GetProductsResponseDto> findProductsByCondition(ProductSearchCondition condition, Pageable pageable);
}
