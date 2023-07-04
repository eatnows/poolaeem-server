package com.poolaeem.poolaeem.security.jwt.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.jwt.service.CustomUserDetailsService;
import com.poolaeem.poolaeem.security.jwt.token.CustomUserDetail;
import com.poolaeem.poolaeem.security.jwt.token.NonLoggedInUserDetail;
import com.poolaeem.poolaeem.security.jwt.token.PostAuthenticationToken;
import com.poolaeem.poolaeem.security.jwt.token.PreAuthenticationToken;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationProvider(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
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
        String email = decodedJWT.getClaim("email").asString();
        String name = decodedJWT.getClaim("name").asString();
        String role = decodedJWT.getClaim("role").asString();

        CustomUserDetail userDetails = new CustomUserDetail(new UserVo(
                userId,
                email,
                name,
                role == null ? UserRole.ROLE_USER : UserRole.ROLE_ADMIN,
                null,
                null,
                null,
                null
        ));

//        UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getClaim("code").asString());

        return new PostAuthenticationToken(userDetails);
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
