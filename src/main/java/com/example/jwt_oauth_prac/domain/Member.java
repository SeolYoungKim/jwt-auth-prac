package com.example.jwt_oauth_prac.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String picture;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Builder
    public Member(String email, String name, String picture, RoleType roleType) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.roleType = roleType;
    }
}
