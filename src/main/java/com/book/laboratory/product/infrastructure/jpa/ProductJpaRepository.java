package com.book.laboratory.product.infrastructure.jpa;

import com.book.laboratory.product.domain.entity.Product;
import com.book.laboratory.product.domain.repository.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long>, ProductRepository {
}
