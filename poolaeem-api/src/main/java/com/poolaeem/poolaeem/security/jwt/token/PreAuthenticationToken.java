package com.poolaeem.poolaeem.security.jwt.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PreAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public PreAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
