package com.poolaeem.poolaeem.user.domain.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class SignUpOAuth2UserServiceImpl implements SignUpOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

        return oAuth2UserService.loadUser(userRequest);
    }
}
