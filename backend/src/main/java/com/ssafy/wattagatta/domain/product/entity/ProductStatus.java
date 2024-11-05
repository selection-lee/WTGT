package com.ssafy.wattagatta.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProductStatus {
    PENDING_ARRIVAL("입고 대기 중"),
    PENDING_TRANSPORT("운반 대기 중"),
    IN_TRANSIT("운반 중"),
    LOADED("적재 완료");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }
}
