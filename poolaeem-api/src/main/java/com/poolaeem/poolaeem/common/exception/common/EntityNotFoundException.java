package com.poolaeem.poolaeem.common.exception.common;

import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class EntityNotFoundException extends ServiceException {

    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.ENTITY_NOT_FOUND;

    public EntityNotFoundException() {
        super(RESPONSE_CODE.getMessage());
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
