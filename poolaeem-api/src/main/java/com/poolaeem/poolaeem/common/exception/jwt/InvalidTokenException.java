package com.poolaeem.poolaeem.common.exception.jwt;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {
    private static final ApiResponseCode apiResponseCode = ApiResponseCode.INVALID_TOKEN;

    public InvalidTokenException(String msg) {
        super(msg);
    }

    public InvalidTokenException() {
        super(apiResponseCode.getMessage());
    }

    public ApiResponseCode getApiResponseCode() {
        return apiResponseCode;
    }
}
