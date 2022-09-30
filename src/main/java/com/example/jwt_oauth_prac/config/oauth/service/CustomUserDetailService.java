package com.example.jwt_oauth_prac.config.oauth.service;

import com.example.jwt_oauth_prac.config.oauth.OAuthAttributes;
import com.example.jwt_oauth_prac.domain.Member;
import com.example.jwt_oauth_prac.domain.RoleType;
import com.example.jwt_oauth_prac.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.jwt_oauth_prac.domain.RoleType.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    /**
     * OAuth2LoginAuthenticationFilter -> AuthenticationManager로 인증 위임
     * -> AuthenticationManager가 AuthenticationProvider의 인증 로직을 이용해서 인증을 해야됨.
     * -> Provider들 중에서, UserDetailsService중에 하나인 여기 로직에 인증 위임
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // google, naver, kakao, github 이라는 이름이 될 아이디들
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();

        // 유저의 이름이나 식별자에 접근할 때 사용함
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 로그인한 OAuth2 유저를 공통 key를 가진 attr을 넣어주고 반환해주기 위함.
        // "sub" = "email주소", ... 이런식으로 key값을 전부 통일
        OAuthAttributes attr
                = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());

        Member member = memberRepository.findByAuthId(attr.getAuthId())
                .orElse(null);


        // 로그인을 한 유저의 정보를 다음과 같이 반환하는 것임.
        // -> 커스터마이징 해서, 기존의 유저 정보에서 토큰 정보를 불러와야함 혹은 신규 유저일 경우 토큰을 추가해주는 로직을 수행해야 함
        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(
                                member == null ? GUEST.getKey() : member.getRoleTypeKey())),
                attr.parsedAttributes(),
                "id" // 이 값은, attribute에 접근 가능한 key 값이다. 지금은 형식을 전부 통일했으므로 id로 변경했다. 문제 시 다시설계함.
        );
    }
}
