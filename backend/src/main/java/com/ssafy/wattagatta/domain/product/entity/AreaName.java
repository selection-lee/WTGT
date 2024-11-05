package com.ssafy.wattagatta.domain.product.entity;

import lombok.Getter;

@Getter
public enum AreaName {
    SEOUL("서울특별시"),
    BUSAN("부산광역시"),
    DAEJEON("대전광역시"),
    GWANGJU("광주광역시"),
    ULSAN("울산광역시"),
    INCHEON("인천광역시"),
    DAEGU("대구광역시"),
    GYEONGGI("경기도"),
    GANGWON("강원도"),
    GYEONGSANGNAM("경상남도"),
    GYEONGSANGBUK("경상북도"),
    JEONLANAM("전라남도"),
    JEONLABUK("전라북도"),
    CHUNGCHEONGNAM("충청남도"),
    CHUNGCHEONGBUK("충청북도"),
    JEJU("제주특별자치도");

    private final String description;

    AreaName(String description) {
        this.description = description;
    }
}
