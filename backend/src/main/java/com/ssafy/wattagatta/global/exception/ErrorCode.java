package com.ssafy.wattagatta.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Member - 1000
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 2000, "해당 ID의 사용자를 찾을 수 없습니다."),

    // 중복 검증
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, 2001, "이미 존재하는 사용자명입니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;
}
