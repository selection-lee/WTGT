package com.ssafy.wattagatta.domain.product.entity;

import lombok.Getter;

@Getter
public enum BaseSector {
    A("서울특별시"),
    B("부산광역시"),
    C("대전광역시"),
    D("광주광역시"),
    E("울산광역시"),
    F("인천광역시"),
    G("대구광역시"),
    H("경기도"),
    I("강원도"),
    J("경상남도"),
    K("경상북도"),
    L("전라남도"),
    M("전라북도"),
    N("충청남도"),
    O("충청북도"),
    P("제주특별자치도"),
    Q("기타 지역 1"),  // 기타 지역을 나타낼 수 있습니다.
    R("기타 지역 2");

    private final String description;

    BaseSector(String description) {
        this.description = description;
    }
}

