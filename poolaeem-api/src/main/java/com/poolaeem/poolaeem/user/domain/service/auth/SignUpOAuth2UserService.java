package com.poolaeem.poolaeem.user.domain.service.auth;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * 테스트를 위해 인터페이스 추가
 */
public interface SignUpOAuth2UserService {
    OAuth2User loadUser(OAuth2UserRequest userRequest);
}
