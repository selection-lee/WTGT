package com.ssafy.wattagatta.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Member - 1000
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 1000, "해당 ID의 사용자를 찾을 수 없습니다."),

    // 중복 검증
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, 2001, "이미 존재하는 사용자명입니다."),

    // 로그인 실패
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, 2002, "아이디 또는 비밀번호가 올바르지 않습니다.");

    // Product - 3000
    INVALID_REGION_CODE(HttpStatus.BAD_REQUEST, 3000, "유효하지 않은 지역 코드입니다."),

    // Agent - 4000
    CANNOT_FIND_NEW_PATH(HttpStatus.NOT_FOUND, 4000, "에이전트의 경로를 찾지 못했습니다");
    private final HttpStatus status;
    private final int code;
    private final String message;
}
