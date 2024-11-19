package com.ssafy.wattagatta.domain.order.repository;

import com.ssafy.wattagatta.domain.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
}
