package com.ssafy.wattagatta.domain.auth.service;

import com.ssafy.wattagatta.domain.auth.dto.SignUpRequest;
import com.ssafy.wattagatta.domain.auth.dto.SignUpResponse;
import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
import com.ssafy.wattagatta.domain.member.repository.MemberRepository;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponse signUp(SignUpRequest request) {
        // 1. 사용자명 중복 검증
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_USERNAME);
        }

        // 2. 비밀번호 암호화 및 회원 엔티티 생성
        MemberEntity member = MemberEntity.createUser(
                request.getUsername(),
                request.getNickname(),
                passwordEncoder.encode(request.getPassword())
        );

        // 3. DB에 저장
        memberRepository.save(member);

        // 4. 응답 생성
        return new SignUpResponse(member.getUsername(), member.getNickname());
    }
}
