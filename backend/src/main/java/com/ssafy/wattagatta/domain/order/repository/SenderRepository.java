package com.ssafy.wattagatta.domain.order.repository;

import com.ssafy.wattagatta.domain.invoice.entity.SenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SenderRepository extends JpaRepository<SenderEntity, Integer> {
}
