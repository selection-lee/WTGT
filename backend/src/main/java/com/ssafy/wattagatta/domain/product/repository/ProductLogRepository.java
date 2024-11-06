package com.ssafy.wattagatta.domain.product.repository;

import com.ssafy.wattagatta.domain.product.entity.ProductLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLogRepository extends JpaRepository<ProductLogEntity, Integer> {
}
