package com.example.jwt_oauth_prac.web.controller;

import com.example.jwt_oauth_prac.domain.Member;
import com.example.jwt_oauth_prac.domain.RoleType;
import com.example.jwt_oauth_prac.jwt.JwtProvider;
import com.example.jwt_oauth_prac.jwt.dto.JwtDto;
import com.example.jwt_oauth_prac.repository.MemberRepository;
import com.example.jwt_oauth_prac.config.oauth.handler.dto.LoginFailureDto;
import com.example.jwt_oauth_prac.config.oauth.handler.dto.LoginSuccessDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;


    @GetMapping("/signup")  // 원래는 @RequestBody로 JSON 받아서 처리해야 함
    public Member signup(@AuthenticationPrincipal OAuth2User oAuth2User) {

        //TODO: 토큰을 발급 해주고, 저장 해주자.
        JwtDto jwtDto = jwtProvider.generateJwtDto(oAuth2User);

        log.info("지금 로그인한 사람 ? = {}", oAuth2User.toString());

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 원래는 서비스 계층에서 해야됨. 원래는 DTO 써야됨 ㅠ
        return memberRepository.save(Member.builder()
                .authId((String) attributes.get("id"))
                .email((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .roleType(RoleType.USER)
                .accessToken(jwtDto.getAccessToken())
                .refreshToken(jwtDto.getRefreshToken())
                .build());

    }

    // TODO: 여기로 요청을 보냈을 때, TOKEN 발급 및 검증이 되도록 하자. -> 필터에서 토큰을 발급 및 검증 하고, 해당 Controller에서 사용하도록 해볼까?
    @GetMapping("/auth")
    public Member auth(UsernamePasswordAuthenticationToken user) {
        log.info("이거 되니 ? = {}", user.toString());
        log.info("user 이름은 ? = {}", user.getName());

        return memberRepository.findByAuthId(user.getName())
                .orElseThrow(IllegalAccessError::new);
    }
}
