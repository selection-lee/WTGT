package com.ssafy.wattagatta.domain.member.service;

import com.ssafy.wattagatta.domain.auth.dto.SignUpDto;
import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
import com.ssafy.wattagatta.domain.member.entity.Role;
import com.ssafy.wattagatta.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SignUpService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void signUpProcess(SignUpDto signUpDto) {
        String username = signUpDto.getUsername();
        String password = signUpDto.getPassword();
        String nickname = signUpDto.getNickname();

        Boolean isExist = memberRepository.existsByUsername(username);

        if (isExist) {
            return;
        }

        MemberEntity data = new MemberEntity(
                username,
                nickname,
                bCryptPasswordEncoder.encode(password),
                Role.ADMIN
        );

        memberRepository.save(data);
    }
}
