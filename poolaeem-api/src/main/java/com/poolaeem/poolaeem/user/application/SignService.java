package com.poolaeem.poolaeem.user.application;

import com.poolaeem.poolaeem.user.domain.entity.User;


public interface SignService {
    User signUpOAuth2User(String oauthProvider, String oauthId);
}
