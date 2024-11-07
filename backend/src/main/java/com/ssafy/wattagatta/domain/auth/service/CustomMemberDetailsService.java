package com.ssafy.wattagatta.domain.auth.service;

import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
import com.ssafy.wattagatta.domain.member.repository.MemberRepository;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomMemberDetailsService(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DB에서 조회
//        MemberEntity memberData = (MemberEntity) memberRepository.findByUsername(username);
        MemberEntity memberData = memberRepository.findByUsername(username);

        if (memberData == null) {
            // 사용자를 찾지 못했을 때 CustomException 발생
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        // UserDetails에 담아서 return하면 AutneticationManager가 검증 함
        return new CustomMemberDetails(memberData);
    }
}
