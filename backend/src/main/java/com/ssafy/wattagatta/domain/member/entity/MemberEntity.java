package com.ssafy.wattagatta.domain.member.entity;

import com.ssafy.wattagatta.domain.order.entity.OrderEntity;
import com.ssafy.wattagatta.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
//@Builder // builder 패턴 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor // 빌더 패턴 사용하기 위함
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "role", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    //    @Builder.Default // 빌더패턴에서 기본값 설정
    @OneToMany(mappedBy = "memberEntity", fetch = FetchType.LAZY)
    private List<OrderEntity> orders = new ArrayList<>();

    // 생성자로 객체 생성
    public MemberEntity(String username, String nickname, String password, Role role) {
//        return MemberEntity.builder()
//                .username(username)
//                .nickname(nickname)
//                .password(encodedPassword)
//                .role(Role.USER)
//                .build();
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

    // 정적 팩토리 메서드
    public static MemberEntity createUser(String username, String nickname, String password) {
        return new MemberEntity(username, nickname, password, Role.USER);
    }
}
