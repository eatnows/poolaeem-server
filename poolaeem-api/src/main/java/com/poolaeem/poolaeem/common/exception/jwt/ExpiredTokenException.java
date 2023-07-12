package com.poolaeem.poolaeem.common.exception.jwt;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import org.springframework.security.core.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {

    public ExpiredTokenException(String msg) {
        super(msg);
    }

    public ExpiredTokenException() {
        super(ApiResponseCode.EXPIRED_TOKEN.getMessage());
    }
}
