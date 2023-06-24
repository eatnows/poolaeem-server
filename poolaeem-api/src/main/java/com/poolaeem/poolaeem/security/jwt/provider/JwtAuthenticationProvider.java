package com.poolaeem.poolaeem.security.jwt.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.jwt.token.CustomUserDetail;
import com.poolaeem.poolaeem.security.jwt.token.PostAuthenticationToken;
import com.poolaeem.poolaeem.security.jwt.token.PreAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationProvider(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if ("ROLE_ANONYMOUS".equals(authentication.getDetails())) {
            // TODO 비 로그인일 경우 가짜 유저 객체를 생성
        }
        String token = (String) authentication.getPrincipal();
        DecodedJWT decodedJWT = validToken(token);

        // TODO 유저를 찾는다.
        // TODO CustomUserDetail 을 만듦
        CustomUserDetail customUserDetail = new CustomUserDetail(token);
        return new PostAuthenticationToken(customUserDetail);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private DecodedJWT validToken(String token) {
        Optional<DecodedJWT> decodedJWT = jwtTokenUtil.validAccessToken(token);

        return decodedJWT.get();
    }
}
