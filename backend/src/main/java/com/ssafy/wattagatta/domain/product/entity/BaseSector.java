package com.ssafy.wattagatta.domain.product.entity;

import com.ssafy.wattagatta.domain.product.dto.TargetLoc;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import lombok.Getter;

/**
 * 위치와 해당 위치에 적재하기 위한 좌표 A B C D E 만 사용
 */
@Getter
public enum BaseSector {
    A("서울특별시", new TargetLoc(8, 0)),
    B("부산광역시", new TargetLoc(8, 1)),
    C("대전광역시", new TargetLoc(8, 2)),
    D("경상북도", new TargetLoc(8, 3)),
    E("광주광역시", new TargetLoc(8, 5)),
    F("인천광역시", new TargetLoc(8, 6)),
    G("대구광역시", new TargetLoc(8, 7)),
    H("경기도", new TargetLoc(8, 8)),
    I("강원도", new TargetLoc(9, 9)),
    J("경상남도", new TargetLoc(9, 9)),
    K("울산광역시", new TargetLoc(9, 9)),
    L("전라남도", new TargetLoc(9, 9)),
    M("전라북도", new TargetLoc(9, 9)),
    N("충청남도", new TargetLoc(9, 9)),
    O("충청북도", new TargetLoc(9, 9)),
    P("제주특별자치도", new TargetLoc(9, 9)),
    Q("기타 지역 1", new TargetLoc(9, 9)),
    R("기타 지역 2", new TargetLoc(9, 9));

    private final String description;
    private final TargetLoc location;

    BaseSector(String description, TargetLoc location) {
        this.description = description;
        this.location = location;
    }

    public static TargetLoc getLocationByRegionCode(String regionCode) {
        try {
            BaseSector sector = BaseSector.valueOf(regionCode);
            return sector.getLocation();
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_REGION_CODE);
        }
    }
}

