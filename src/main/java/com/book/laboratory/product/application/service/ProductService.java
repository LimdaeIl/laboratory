package com.book.laboratory.product.application.service;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.product.application.dto.requset.CreateProductRequestDto;
import com.book.laboratory.product.application.dto.response.CreateProductResponseDto;

public interface ProductService {
  CreateProductResponseDto createProduct(CreateProductRequestDto requestDto, CustomUserDetails userDetails);
}
