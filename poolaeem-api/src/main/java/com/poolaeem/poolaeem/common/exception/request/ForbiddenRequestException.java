package com.poolaeem.poolaeem.common.exception.request;

import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class ForbiddenRequestException extends ServiceException {
    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.FORBIDDEN;

    public ForbiddenRequestException() {
        super(RESPONSE_CODE.getMessage());
    }

    public ForbiddenRequestException(String message) {
        super(message);
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
