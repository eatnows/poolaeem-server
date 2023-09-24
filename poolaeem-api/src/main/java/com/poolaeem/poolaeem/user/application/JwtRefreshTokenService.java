package com.poolaeem.poolaeem.user.application;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtRefreshTokenService {
    void addRefreshToken(String userId, String refreshToken, HttpServletRequest request);

    DecodedJWT validRefreshToken(String refreshToken, HttpServletRequest request);
}
