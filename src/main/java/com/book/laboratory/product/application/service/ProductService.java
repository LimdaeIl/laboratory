package com.book.laboratory.product.application.service;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.product.application.dto.condition.ProductSearchCondition;
import com.book.laboratory.product.application.dto.request.CreateProductRequestDto;
import com.book.laboratory.product.application.dto.request.UpdateProductRequestDto;
import com.book.laboratory.product.application.dto.response.CreateProductResponseDto;
import com.book.laboratory.product.application.dto.response.DeleteProductResponseDto;
import com.book.laboratory.product.application.dto.response.GetProductResponseDto;
import com.book.laboratory.product.application.dto.response.GetProductsResponseDto;
import com.book.laboratory.product.application.dto.response.UpdateProductResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
  CreateProductResponseDto createProduct(CreateProductRequestDto requestDto, CustomUserDetails userDetails);

  GetProductResponseDto getProduct(UUID id);

  Page<GetProductsResponseDto> getProducts(ProductSearchCondition condition, Pageable page);

  UpdateProductResponseDto updateProduct(UpdateProductRequestDto requestDto, CustomUserDetails userDetails, UUID id);

  DeleteProductResponseDto deleteProduct(CustomUserDetails userDetails, UUID id);
}
