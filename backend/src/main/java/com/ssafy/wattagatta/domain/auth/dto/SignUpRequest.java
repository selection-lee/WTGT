package com.ssafy.wattagatta.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    @NotBlank(message = "사용자 이름은 필수입니다")
    private String username;

    //    @NotBlank(message = "닉네임은 필수입니다")
    private String nickname;

    //    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)[A-Za-z\\\\d]{8,}$",
//            message = "비밀번호는 8자 이상의 영문자와 숫자 조합이어야 합니다.")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

}
