package com.example.jwt_oauth_prac.web.controller;

import com.example.jwt_oauth_prac.domain.Member;
import com.example.jwt_oauth_prac.domain.RoleType;
import com.example.jwt_oauth_prac.repository.MemberRepository;
import com.example.jwt_oauth_prac.web.dto.LoginFailureDto;
import com.example.jwt_oauth_prac.web.dto.LoginSuccessDto;
import com.example.jwt_oauth_prac.web.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository memberRepository;

    @GetMapping("/login-failure")
    public LoginFailureDto loginFailureDto(@AuthenticationPrincipal OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String authId = (String) attributes.get("id");

        return LoginFailureDto.builder()
                .id(authId)
                .loginSuccess(false)
                .build();  // 이 정보를 프론트에 내려준다.
    }

    @GetMapping("/login-success")
    public LoginSuccessDto loginSuccessDto(@AuthenticationPrincipal OAuth2User oAuth2User,
                                           HttpServletRequest request) {

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String authId = (String) attributes.get("id");

        String jwt = request.getHeader("Authentication");  // 헤더에 세팅이 안됐네..

        return LoginSuccessDto
                .builder()
                .id(authId)
                .name((String) attributes.get("name"))
                .email((String) attributes.get("sub"))
                .picture((String) attributes.get("picture"))
                .loginSuccess(true)
                .accessToken(jwt)
                .build();
    }

    @GetMapping("/signup")  // 원래는 @RequestBody로 JSON 받아서 처리해야 함
    public Member signup(@AuthenticationPrincipal OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 원래는 서비스 계층에서 해야됨. 원래는 DTO 써야됨 ㅠ
        return memberRepository.save(Member.builder()
                .authId((String) attributes.get("id"))
                .email((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .roleType(RoleType.USER)
                .refreshToken("refreshToken")
                .build());

    }
}
