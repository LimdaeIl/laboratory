package com.book.laboratory.product.presentation;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.product.application.dto.requset.CreateProductRequestDto;
import com.book.laboratory.product.application.dto.response.CreateProductResponseDto;
import com.book.laboratory.product.application.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

  private final ProductService productService;

  @PreAuthorize("hasAnyRole('ADMIN', 'STORE')")
  @PostMapping
  public ResponseEntity<CreateProductResponseDto> createProduct(
      @RequestBody @Valid CreateProductRequestDto requestDto,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    CreateProductResponseDto responseDto = productService.createProduct(requestDto, userDetails);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDto);
  }
}
