package com.example.jwt_oauth_prac.config.auth;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 각각 다른 곳에서 로그인을 요청한 유저의 attributes 정보를 평준화 하는 클래스
 */

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String userName;
    private final String userEmail;
    private final String userPicture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String userName, String userEmail, String userPicture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPicture = userPicture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        if ("naver".equals(registrationId)) { // 이는 yml 파일에 적어준 registration id임. google, naver, kakao 셋중 하나임
            return ofNaver("id", attributes);
        }

        if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .userName((String) attributes.get("name"))
                .userEmail((String) attributes.get("email"))
                .userPicture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .userName((String) response.get("name"))
                .userEmail((String) response.get("email"))
                .userPicture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        return OAuthAttributes.builder()
                .userName((String) profile.get("nickname"))
                .userEmail((String) response.get("email"))
                .userPicture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // Map으로 바꿔주는 작업을 해야함. 그래야 DefaultOAuth2User를 똑같은 attribute key를 가진 객체로 바꿀 수 있음
    // Missing attribute 'sub' in attributes -> "sub"이라는 키의 attribute가 있어야 하는듯 ?
    public Map<String, Object> parsedAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put("sub", userEmail);
        map.put("name", userName);
        map.put("picture", userPicture);

        return map;
    }

}
