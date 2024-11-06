package com.ssafy.wattagatta.domain.product.repository;

import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    List<ProductEntity> findByExpectedArrivalDate(LocalDate date);
}
