package com.ssafy.wattagatta.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Member - 1000
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 1000, "해당 ID의 사용자를 찾을 수 없습니다."),

    // Product - 3000
    INVALID_REGION_CODE(HttpStatus.BAD_REQUEST, 3000, "유효하지 않은 지역 코드입니다."),

    // Agent - 4000
    CANNOT_FIND_NEW_PATH(HttpStatus.NOT_FOUND, 4000, "에이전트의 경로를 찾지 못했습니다");
    private final HttpStatus status;
    private final int code;
    private final String message;
}
