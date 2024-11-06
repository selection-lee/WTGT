package com.ssafy.wattagatta.domain.auth.controller;

import com.ssafy.wattagatta.domain.auth.dto.SignUpRequest;
import com.ssafy.wattagatta.domain.auth.dto.SignUpResponse;
import com.ssafy.wattagatta.domain.auth.service.AuthService;
import com.ssafy.wattagatta.global.utils.ApiUtils;
import com.ssafy.wattagatta.global.utils.ApiUtils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated // 유효성 검증
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResult<SignUpResponse>> signup(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);
        return ResponseEntity.ok(ApiUtils.success(response));
    }
}
