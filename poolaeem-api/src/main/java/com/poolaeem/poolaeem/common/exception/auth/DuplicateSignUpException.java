package com.poolaeem.poolaeem.common.exception.auth;

import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class DuplicateSignUpException extends ServiceException {

    public DuplicateSignUpException(String message) {
        super(message);
    }

    public DuplicateSignUpException() {
        super(ApiResponseCode.DUPLICATE_SIGNED_UP.getMessage());
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return ApiResponseCode.DUPLICATE_SIGNED_UP;
    }
}
