package com.poolaeem.poolaeem.common.exception.word;

import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class NotSupportLanguageException extends ServiceException {
    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.NOT_SUPPORT;
    public NotSupportLanguageException() {
        super(RESPONSE_CODE.getMessage());
    }

    public NotSupportLanguageException(String message) {
        super(message);
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
