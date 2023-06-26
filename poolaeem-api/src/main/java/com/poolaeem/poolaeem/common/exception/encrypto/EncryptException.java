package com.poolaeem.poolaeem.common.exception.encrypto;

import com.poolaeem.poolaeem.common.exception.base.ServiceErrorException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class EncryptException extends ServiceErrorException {

    private static final ApiResponseCode apiResponseCode = ApiResponseCode.ENCRYPT_ERROR;

    public EncryptException() {
        super(apiResponseCode.getMessage());
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return apiResponseCode;
    }
}
