package com.example.jwt_oauth_prac.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String authId;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String picture;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    //TODO: 토큰 값을 저장하는 곳이 필요하다. 아마 리프레쉬 토큰만 이용할 것으로 사료된다.
    // 설계를 잘 해보도록 하자.
    @Column
    private String refreshToken;

    @Builder
    public Member(String authId, String email, String name, String picture, RoleType roleType, String refreshToken) {
        this.authId = authId;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.roleType = roleType;
        this.refreshToken = refreshToken;
    }
}
