package com.book.laboratory.product.domain.repository;

import com.book.laboratory.product.domain.entity.Product;
import java.util.Optional;

public interface ProductRepository {

  Product save(Product product);

  Optional<Product> findById(Long id);

}
