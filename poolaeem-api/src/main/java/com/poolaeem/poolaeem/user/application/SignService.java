package com.poolaeem.poolaeem.user.application;

import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;


public interface SignService {
    User signUpOAuth2User(HttpServletRequest request, OauthProvider oauthProvider, String oauthId, String email);

    String generateAccessTokenByRefreshToken(HttpServletRequest request, String refreshToken);

    void deleteUser(String tokenUserId, String pathUserId);

    void signOut(String userId);
}
