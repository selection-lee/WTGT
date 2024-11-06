package com.ssafy.wattagatta.domain.member.repository;

import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    // 사용자명 중복 체크
    boolean existsByUsername(String username);
}
