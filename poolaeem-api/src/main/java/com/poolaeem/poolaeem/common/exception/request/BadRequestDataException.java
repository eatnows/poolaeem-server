package com.poolaeem.poolaeem.common.exception.request;

import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class BadRequestDataException extends ServiceException {

    private static final ApiResponseCode apiResponseCode = ApiResponseCode.BAD_REQUEST_DATA;

    public BadRequestDataException() {
        super(apiResponseCode.getMessage());
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return apiResponseCode;
    }
}
