package com.book.laboratory.product.presentation;

import com.book.laboratory.common.security.CustomUserDetails;
import com.book.laboratory.product.application.dto.request.CreateProductRequestDto;
import com.book.laboratory.product.application.dto.request.UpdateProductRequestDto;
import com.book.laboratory.product.application.dto.response.CreateProductResponseDto;
import com.book.laboratory.product.application.dto.response.DeleteProductResponseDto;
import com.book.laboratory.product.application.dto.response.UpdateProductResponseDto;
import com.book.laboratory.product.application.service.ProductService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/products")
@RestController
public class ProductAdminController {

  private final ProductService productService;

  @PreAuthorize("hasAnyRole('ADMIN', 'STORE')")
  @PostMapping
  public ResponseEntity<CreateProductResponseDto> createProduct(
      @RequestBody @Valid CreateProductRequestDto requestDto,
      @AuthenticationPrincipal CustomUserDetails userDetails
  ) {
    CreateProductResponseDto responseDto = productService.createProduct(requestDto, userDetails);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(responseDto);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'STORE')")
  @PatchMapping("/{id}")
  public ResponseEntity<UpdateProductResponseDto> updateProduct(
      @RequestBody @Valid UpdateProductRequestDto requestDto,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable UUID id
  ) {
    UpdateProductResponseDto responseDto = productService.updateProduct(requestDto, userDetails, id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDto);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'STORE')")
  @DeleteMapping("/{id}")
  public ResponseEntity<DeleteProductResponseDto> deleteProduct(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable UUID id
  ) {
    DeleteProductResponseDto responseDto = productService.deleteProduct(userDetails, id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(responseDto);
  }
}
