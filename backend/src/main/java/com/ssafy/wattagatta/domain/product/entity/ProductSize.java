package com.ssafy.wattagatta.domain.product.entity;

import lombok.Getter;

@Getter
public enum ProductSize {
    SMALL("소형"),
    LARGE("대형");

    private final String description;

    ProductSize(String description) {
        this.description = description;
    }
}
