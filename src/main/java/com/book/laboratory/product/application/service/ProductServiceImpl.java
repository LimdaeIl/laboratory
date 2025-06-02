package com.book.laboratory.product.application.service;

import com.book.laboratory.common.exception.CustomException;
import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.product.application.dto.condition.ProductSearchCondition;
import com.book.laboratory.product.application.dto.request.CreateProductRequestDto;
import com.book.laboratory.product.application.dto.request.UpdateProductRequestDto;
import com.book.laboratory.product.application.dto.response.CreateProductResponseDto;
import com.book.laboratory.product.application.dto.response.DeleteProductResponseDto;
import com.book.laboratory.product.application.dto.response.GetProductResponseDto;
import com.book.laboratory.product.application.dto.response.GetProductsResponseDto;
import com.book.laboratory.product.application.dto.response.UpdateProductResponseDto;
import com.book.laboratory.product.domain.entity.Product;
import com.book.laboratory.product.domain.entity.ProductErrorCode;
import com.book.laboratory.product.domain.repository.ProductQueryRepository;
import com.book.laboratory.product.domain.repository.ProductRepository;
import com.book.laboratory.user.domain.user.User;
import com.book.laboratory.user.domain.user.UserErrorCode;
import com.book.laboratory.user.domain.user.UserRepository;
import com.book.laboratory.user.domain.user.UserRole;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final ProductQueryRepository productQueryRepository;

  private Product findProductById(UUID id) {
    return productRepository.findProductById(id)
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
        .thumbnail(requestDto.thumbnail().isBlank() ? null : requestDto.thumbnail())
        .description(requestDto.description())
        .status(requestDto.status())
        .category(requestDto.category())
        .build();

    Product savedProduct = productRepository.save(productBuild);

    return CreateProductResponseDto.from(savedProduct);
  }

  @Transactional(readOnly = true)
  @Override
  public GetProductResponseDto getProduct(UUID id) {
    Product productById = findProductById(id);
    Long createdBy = productById.getCreatedBy();

    User user = userRepository.findUserById(createdBy)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND_BY_ID));

    return GetProductResponseDto.from(productById, user);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<GetProductsResponseDto> getProducts(ProductSearchCondition condition, Pageable page) {
    return productQueryRepository.findProductsByCondition(condition, page);
  }

  @Transactional
  @Override
  public UpdateProductResponseDto updateProduct(UpdateProductRequestDto requestDto,
                                                CustomUserDetails userDetails,
                                                UUID id) {

    Product product = findProductById(id);

    if (userDetails.role() != UserRole.ROLE_ADMIN &&
        !product.getCreatedBy().equals(userDetails.id())) {
      throw new CustomException(ProductErrorCode.PRODUCT_UPDATE_FORBIDDEN);
    }

    if (requestDto.name() != null) {
      product.changeNameTo(requestDto.name());
    }
    if (requestDto.price() != null) {
      product.changePriceTo(requestDto.price());
    }
    if (requestDto.quantity() != null) {
      product.changeQuantityTo(requestDto.quantity());
    }
    if (requestDto.thumbnail() != null) {
      product.updateThumbnail(requestDto.thumbnail());
    }
    if (requestDto.description() != null) {
      product.changeDescriptionTo(requestDto.description());
    }
    if (requestDto.category() != null) {
      product.changeCategoryTo(requestDto.category());
    }

    return UpdateProductResponseDto.from(product);
  }

  @Transactional
  @Override
  public DeleteProductResponseDto deleteProduct(CustomUserDetails userDetails, UUID id) {
    Product productById = findProductById(id);

    if (!userDetails.role().equals(UserRole.ROLE_ADMIN) && !productById.getCreatedBy().equals(userDetails.id())) {
      throw new CustomException(ProductErrorCode.PRODUCT_DELETE_FORBIDDEN);
    }

    productById.markDeleted(userDetails.id());

    return DeleteProductResponseDto.from(productById);
  }


}
