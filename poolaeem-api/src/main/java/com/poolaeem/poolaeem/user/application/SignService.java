package com.poolaeem.poolaeem.user.application;

import com.poolaeem.poolaeem.security.oauth2.model.ProviderUser;
import com.poolaeem.poolaeem.user.domain.entity.User;

import java.util.Optional;

public interface SignService {
    User signUpOAuth2User(String oauthProvider, String oauthProvider1);
}
