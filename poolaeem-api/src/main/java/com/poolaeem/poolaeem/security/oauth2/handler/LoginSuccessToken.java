package com.poolaeem.poolaeem.security.oauth2.handler;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.user.application.JwtRefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;


@Component
public class LoginSuccessToken {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtRefreshTokenService jwtRefreshTokenService;

    public LoginSuccessToken(JwtTokenUtil jwtTokenUtil, JwtRefreshTokenService jwtRefreshTokenService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtRefreshTokenService = jwtRefreshTokenService;
    }

    public void addTokenInResponse(HttpServletRequest request, HttpServletResponse response, GenerateTokenUser generateTokenUser) {

        String accessToken = jwtTokenUtil.generateAccessToken(generateTokenUser);
        String refreshToken = jwtTokenUtil.generateRefreshToken(generateTokenUser);

        response.addHeader("Access-Control-Expose-Headers", "access-token, refresh-token");
        response.addHeader("access-token", accessToken);
        response.addHeader("refresh-token", refreshToken);

        jwtRefreshTokenService.addRefreshToken(generateTokenUser.getId(), refreshToken, request);
    }

    public void addOnlyAccessTokenInResponse(HttpServletResponse response, String accessToken) {
        response.addHeader("Access-Control-Expose-Headers", "access-token");
        response.addHeader("access-token", accessToken);
    }
}
