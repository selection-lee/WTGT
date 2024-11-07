package com.ssafy.wattagatta.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpDto {
    private String username;
    private String password;
    private String nickname;
}
