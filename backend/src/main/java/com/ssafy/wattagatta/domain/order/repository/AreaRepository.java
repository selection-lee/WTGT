package com.ssafy.wattagatta.domain.order.repository;

import com.ssafy.wattagatta.domain.product.entity.AreaEntity;
import com.ssafy.wattagatta.domain.product.entity.AreaName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<AreaEntity, Integer> {
    AreaEntity findByAreaName(AreaName areaName);
}
