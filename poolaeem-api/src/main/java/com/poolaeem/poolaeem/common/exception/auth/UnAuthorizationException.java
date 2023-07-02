package com.poolaeem.poolaeem.common.exception.auth;

import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class UnAuthorizationException extends ServiceException {

    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.UNAUTHORIZED;
    public UnAuthorizationException() {
        super(RESPONSE_CODE.getMessage());
    }

    public UnAuthorizationException(String message) {
        super(message);
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
