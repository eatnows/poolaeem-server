package com.poolaeem.poolaeem.user.application;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtRefreshTokenService {
    void addRefreshToken(String userId, String refreshToken, HttpServletRequest request);
}
