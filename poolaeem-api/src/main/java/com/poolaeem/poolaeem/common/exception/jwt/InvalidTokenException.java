package com.poolaeem.poolaeem.common.exception.jwt;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException(String msg) {
        super(msg);
    }

    public InvalidTokenException() {
        super(ApiResponseCode.INVALID_TOKEN.getMessage());
    }
}
