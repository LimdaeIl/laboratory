package com.book.laboratory.product.domain.repository;

import com.book.laboratory.product.domain.entity.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

  Product save(Product product);

  Optional<Product> findProductById(UUID id);

}
