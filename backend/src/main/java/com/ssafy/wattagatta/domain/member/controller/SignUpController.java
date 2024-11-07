package com.ssafy.wattagatta.domain.member.controller;

import com.ssafy.wattagatta.domain.auth.dto.SignUpDto;
import com.ssafy.wattagatta.domain.member.service.SignUpService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class SignUpController {

    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("/api/signup")
    public String signUpProcess(SignUpDto signUpDto) {
        System.out.println(signUpDto.getUsername());
        signUpService.signUpProcess(signUpDto);

        return "회원가입 요청";
    }
}
