package com.ssafy.wattagatta.domain.product.entity;

import lombok.Getter;

@Getter
public enum Category {
    ELECTRONICS("전자제품"),
    FOOD("식품"),
    FURNITURE("가구"),
    CLOTHING("의류"),
    SPORTS("스포츠용품"),
    BOOKS("도서"),
    STATIONERY("문구류"),
    HOUSEHOLD("생활용품"),
    COSMETICS("화장품"),
    AUTOMOTIVE("자동차용품");

    private final String description;

    Category(String description) {
        this.description = description;
    }
}
