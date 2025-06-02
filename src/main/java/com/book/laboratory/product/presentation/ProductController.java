package com.book.laboratory.product.presentation;

import com.book.laboratory.product.application.dto.response.GetProductResponseDto;
import com.book.laboratory.product.application.service.ProductService;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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



}
