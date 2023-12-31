package com.poolaeem.poolaeem.security.jwt.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.jwt.token.CustomUserDetail;
import com.poolaeem.poolaeem.security.jwt.token.NonLoggedInUserDetail;
import com.poolaeem.poolaeem.security.jwt.token.PostAuthenticationToken;
import com.poolaeem.poolaeem.security.jwt.token.PreAuthenticationToken;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationProvider(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if ("ROLE_ANONYMOUS".equals(authentication.getDetails())) {
            NonLoggedInUserDetail nonLoggedInUserDetail = new NonLoggedInUserDetail();
            return new PostAuthenticationToken(nonLoggedInUserDetail);
        }
        String token = (String) authentication.getPrincipal();
        DecodedJWT decodedJWT = validToken(token);

        String userId = decodedJWT.getClaim("code").asString();
        String role = decodedJWT.getClaim("role").asString();

        CustomUserDetail userDetails = new CustomUserDetail(new UserVo(
                userId,
                null,
                null,
                role == null ? UserRole.ROLE_USER : UserRole.valueOf(role),
                null,
                null,
                null,
                null,
                false));

        return new PostAuthenticationToken(userDetails);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private DecodedJWT validToken(String token) {
        return jwtTokenUtil.validAccessToken(token);
    }
}
