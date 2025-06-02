package com.book.laboratory.product.application.service;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.product.application.dto.requset.CreateProductRequestDto;
import com.book.laboratory.product.application.dto.response.CreateProductResponseDto;
import com.book.laboratory.product.domain.entity.Product;
import com.book.laboratory.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Transactional
  @Override
  public CreateProductResponseDto createProduct(
      CreateProductRequestDto requestDto,
      CustomUserDetails userDetails) {

    Product productBuild = Product.builder()
        .name(requestDto.name())
        .price(requestDto.price())
        .quantity(requestDto.quantity())
        .thumbnail(requestDto.thumbnail())
        .description(requestDto.description())
        .status(requestDto.status())
        .category(requestDto.category())
        .build();

    Product savedProduct = productRepository.save(productBuild);

    return CreateProductResponseDto.from(savedProduct);
  }
}
