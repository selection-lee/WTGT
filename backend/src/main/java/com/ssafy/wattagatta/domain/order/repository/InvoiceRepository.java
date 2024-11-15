package com.ssafy.wattagatta.domain.order.repository;

import com.ssafy.wattagatta.domain.invoice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
}
