package com.poolaeem.poolaeem.security.oauth2.handler;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessToken {

    private final JwtTokenUtil jwtTokenUtil;

    public LoginSuccessToken(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public void addTokenInResponse(HttpServletResponse response, GenerateTokenUser generateTokenUser) throws IOException {

        String accessToken = jwtTokenUtil.generateAccessToken(generateTokenUser);
        String refreshToken = jwtTokenUtil.generateRefreshToken(generateTokenUser);

        response.addHeader("ac", accessToken);
        response.addHeader("rf", refreshToken);

    }
}
