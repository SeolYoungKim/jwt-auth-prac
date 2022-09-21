package com.example.jwt_oauth_prac.config.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailService extends DefaultOAuth2UserService {

    /**
     * 로그인을 한 유저의 정보를 이용해서 -> UserDetails(Authentication)을 커스텀해서 반환해줄 수 있는 로직이다.
     * 여기서 처리를 좀 해서, DefaultOAuth2User를 만들어주자. 안 만들어주면 기본 제공 유저 말고는, 나머지 파싱이 안된다.
     * 여기서 처리하고, 로그인한 유저를 좀 평준화 해서 돌려주면 -> 나머지 로직에서 처리가 될 것이다.
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
        OAuthAttributes attr
                = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attr.parsedAttributes(),
                attr.getNameAttributeKey()
        );
    }
}
