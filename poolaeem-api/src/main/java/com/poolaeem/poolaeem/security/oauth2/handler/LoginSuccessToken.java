package com.poolaeem.poolaeem.security.oauth2.handler;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;


@Component
public class LoginSuccessToken {

    private final JwtTokenUtil jwtTokenUtil;

    public LoginSuccessToken(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public void addTokenInResponse(HttpServletResponse response, GenerateTokenUser generateTokenUser) {

        String accessToken = jwtTokenUtil.generateAccessToken(generateTokenUser);
        String refreshToken = jwtTokenUtil.generateRefreshToken(generateTokenUser);

        response.addHeader("access-token", accessToken);
        response.addHeader("refresh-token", refreshToken);
    }
}
