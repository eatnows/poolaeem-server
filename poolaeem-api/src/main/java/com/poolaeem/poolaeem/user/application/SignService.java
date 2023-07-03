package com.poolaeem.poolaeem.user.application;

import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.User;


public interface SignService {
    User signUpOAuth2User(OauthProvider oauthProvider, String oauthId, String email);
}
