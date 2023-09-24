package com.poolaeem.poolaeem.common.exception.jwt;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import org.springframework.security.core.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {
    private static final ApiResponseCode API_RESPONSE_CODE = ApiResponseCode.EXPIRED_TOKEN;

    public ExpiredTokenException(String msg) {
        super(msg);
    }

    public ExpiredTokenException() {
        super(API_RESPONSE_CODE.getMessage());
    }

    public ApiResponseCode getApiResponseCode() {
        return API_RESPONSE_CODE;
    }
}
