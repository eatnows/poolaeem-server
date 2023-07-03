package com.poolaeem.poolaeem.common.exception.user;

import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class UserNotFoundException extends ServiceException {
    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.USER_NOT_FOUND;

    public UserNotFoundException() {
        super(RESPONSE_CODE.getMessage());
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
