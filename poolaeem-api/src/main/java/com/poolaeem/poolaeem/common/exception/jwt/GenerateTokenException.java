package com.poolaeem.poolaeem.common.exception.jwt;

import com.poolaeem.poolaeem.common.exception.base.ServiceErrorException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class GenerateTokenException extends ServiceErrorException {

    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.GENERATE_TOKEN_ERROR;

    public GenerateTokenException() {
        super(RESPONSE_CODE.getMessage());
    }

    public GenerateTokenException(String message) {
        super(message);
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
