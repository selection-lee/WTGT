package com.ssafy.wattagatta.domain.member.repository;

import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    // 사용자명 중복 체크
    Boolean existsByUsername(String username);

    // username 받아서 db테이블에서 회원을 조회
    MemberEntity findByUsername(String username);
}
