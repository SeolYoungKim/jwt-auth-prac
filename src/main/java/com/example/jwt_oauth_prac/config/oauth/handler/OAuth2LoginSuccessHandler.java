package com.example.jwt_oauth_prac.config.oauth.handler;

import com.example.jwt_oauth_prac.domain.Member;
import com.example.jwt_oauth_prac.jwt.JwtProvider;
import com.example.jwt_oauth_prac.jwt.dto.JwtDto;
import com.example.jwt_oauth_prac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // OAuth2 로그인 성공 시에는 회원 여부를 판단해야 한다.
        OAuth2User user = (OAuth2User) authentication.getPrincipal();  // 현재 OAuth2 인증 유저
        Map<String, Object> attributes = user.getAttributes();
        String authId = (String) attributes.get("id");  // authId를 가져온다. 이걸로 회원가입 여부를 판단할 수 있다.

        Optional<Member> findMember = memberRepository.findByAuthId(authId);

        if (findMember.isEmpty()) {  // 리포지토리에 없으면 최초 로그인 회원이다.
//            response.sendRedirect("/api/test/login-failure");

            String redirectionUri =
                    UriComponentsBuilder.fromUriString("http://localhost:8082")
                    .queryParam("loginSuccess", false)
                    .build()
                    .toUriString();

            response.sendRedirect(redirectionUri);


        } else {
            // 있는 회원의 경우, 찾은 정보를 기반으로 토큰을 발급해준다.
            Member member = findMember.get();
            JwtDto jwtDto = jwtProvider.generateJwtDto(user);

            member.saveToken(jwtDto.getAccessToken(), jwtDto.getRefreshToken());

            String requestURI = request.getRequestURI();

            //TODO: 현재 이 방식은 한가지 문제가 있다. 유저가 어디서 로그인을 요청하던 간에 홈으로 보내버린다는 것이다.

            // 토큰을 쿼리 파라미터로 내려주고, 이를 auth에 요청할 때 헤더로 보내주세요!
            String redirectURI = UriComponentsBuilder.fromUriString("http://localhost:8082")
                    .queryParam("loginSuccess", true)
                    .queryParam("token", jwtDto.getAccessToken())
                    .build()
                    .toUriString();

            response.sendRedirect(redirectURI);
        }

    }
}
