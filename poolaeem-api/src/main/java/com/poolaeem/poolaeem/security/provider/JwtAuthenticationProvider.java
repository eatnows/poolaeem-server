package com.poolaeem.poolaeem.security.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
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
        String token = (String) authentication.getPrincipal();

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }

    private DecodedJWT validToken(String token) {
        Optional<DecodedJWT> decodedJWT = jwtTokenUtil.validAccessToken(token);

        // TODO 유저를 찾는다.
        // TODO CustomUserDetail 을 만듦
        return decodedJWT.get();
    }
}
