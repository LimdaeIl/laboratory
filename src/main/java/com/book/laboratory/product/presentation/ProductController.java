package com.book.laboratory.product.presentation;

import com.book.laboratory.product.application.dto.condition.ProductSearchCondition;
import com.book.laboratory.product.application.dto.response.GetProductResponseDto;
import com.book.laboratory.product.application.dto.response.GetProductsResponseDto;
import com.book.laboratory.product.application.service.ProductService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

  private final ProductService productService;

  @GetMapping("/{id}")
  public ResponseEntity<GetProductResponseDto> getProduct(
      @PathVariable UUID id
  ) {
    GetProductResponseDto responseDto = productService.getProduct(id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDto);
  }

  @GetMapping
  public ResponseEntity<Page<GetProductsResponseDto>> getProducts(
      @ParameterObject ProductSearchCondition condition,
      @PageableDefault(size = 10)
      @SortDefault.SortDefaults({
          @SortDefault(sort = "createdAt", direction = Direction.DESC),
          @SortDefault(sort = "name", direction = Direction.DESC)
      })
      Pageable page
  ) {
    Page<GetProductsResponseDto> responseDtos = productService.getProducts(condition, page);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDtos);
  }


}
