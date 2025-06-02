package com.book.laboratory.product.application.service;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.product.application.dto.requset.CreateProductRequestDto;
import com.book.laboratory.product.application.dto.response.CreateProductResponseDto;
import com.book.laboratory.product.application.dto.response.GetProductResponseDto;
import com.book.laboratory.product.domain.entity.Product;
import com.book.laboratory.product.domain.entity.ProductErrorCode;
import com.book.laboratory.product.domain.repository.ProductRepository;
import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserErrorCode;
import com.book.laboratory.user.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  private Product findProductById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));
  }

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

  @Transactional(readOnly = true)
  @Override
  public GetProductResponseDto getProduct(Long id) {
    Product productById = findProductById(id);
    Long createdBy = productById.getCreatedBy();

    User user = userRepository.findUserById(createdBy)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND_BY_ID));

    return GetProductResponseDto.from(productById, user);
  }
}
