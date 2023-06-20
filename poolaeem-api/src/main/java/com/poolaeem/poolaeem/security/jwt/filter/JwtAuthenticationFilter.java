package com.poolaeem.poolaeem.security.jwt.filter;

import com.poolaeem.poolaeem.common.jwt.HeaderTokenExtractor;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationFailureHandler;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationSuccessHandler;
import com.poolaeem.poolaeem.security.jwt.token.PreAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.UUID;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private JwtAuthenticationSuccessHandler successHandler;
    private JwtAuthenticationFailureHandler failureHandler;
    private HeaderTokenExtractor headerTokenExtractor;

    private final String AUTHORIZATION = "Authorization";

    public JwtAuthenticationFilter(RequestMatcher matcher,
                                   JwtAuthenticationSuccessHandler successHandler,
                                   JwtAuthenticationFailureHandler failureHandler,
                                   HeaderTokenExtractor headerTokenExtractor) {
        super(matcher);

        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.headerTokenExtractor = headerTokenExtractor;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.isEmpty(bearerToken)) {
            String extractToken = headerTokenExtractor.extract(bearerToken);
            PreAuthenticationToken authorizationToken = new PreAuthenticationToken(extractToken, extractToken.length());

            super.getAuthenticationManager().authenticate(authorizationToken);
        }

        PreAuthenticationToken authorizationToken = new PreAuthenticationToken(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        authorizationToken.setDetails("ROLE_ANONYMOUS");

        return super.getAuthenticationManager().authenticate(authorizationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        this.unsuccessfulAuthentication(request, response, failed);
    }
}
