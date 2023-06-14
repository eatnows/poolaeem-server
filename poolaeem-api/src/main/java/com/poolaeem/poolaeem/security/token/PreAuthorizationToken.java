package com.poolaeem.poolaeem.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {
    public PreAuthorizationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
