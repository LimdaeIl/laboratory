package com.book.laboratory.product.application.service;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.product.application.dto.condition.ProductSearchCondition;
import com.book.laboratory.product.application.dto.requset.CreateProductRequestDto;
import com.book.laboratory.product.application.dto.response.CreateProductResponseDto;
import com.book.laboratory.product.application.dto.response.GetProductResponseDto;
import com.book.laboratory.product.application.dto.response.GetProductsResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
  CreateProductResponseDto createProduct(CreateProductRequestDto requestDto, CustomUserDetails userDetails);

  GetProductResponseDto getProduct(UUID id);

  Page<GetProductsResponseDto> getProducts(ProductSearchCondition condition, Pageable page);
}
