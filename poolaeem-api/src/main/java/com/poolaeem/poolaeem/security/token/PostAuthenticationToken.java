package com.poolaeem.poolaeem.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PostAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public PostAuthenticationToken(UserDetails userDetails) {
        super(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
