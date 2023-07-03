package com.poolaeem.poolaeem.common.exception.encrypto;

import com.poolaeem.poolaeem.common.exception.base.ServiceErrorException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class DecryptException extends ServiceErrorException {

    private static final ApiResponseCode apiResponseCode = ApiResponseCode.DECRYPT_ERROR;

    public DecryptException() {
        super(apiResponseCode.getMessage());
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return apiResponseCode;
    }
}
