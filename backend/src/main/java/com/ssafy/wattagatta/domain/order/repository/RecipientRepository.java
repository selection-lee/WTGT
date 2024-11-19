package com.ssafy.wattagatta.domain.order.repository;

import com.ssafy.wattagatta.domain.invoice.entity.RecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipientRepository extends JpaRepository<RecipientEntity, Integer> {
}
