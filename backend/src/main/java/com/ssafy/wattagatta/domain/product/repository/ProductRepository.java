package com.ssafy.wattagatta.domain.product.repository;

import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
}
