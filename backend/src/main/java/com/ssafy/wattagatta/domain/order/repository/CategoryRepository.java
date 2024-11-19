package com.ssafy.wattagatta.domain.order.repository;

import com.ssafy.wattagatta.domain.product.entity.Category;
import com.ssafy.wattagatta.domain.product.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    CategoryEntity findByCategoryName(Category category);
}
