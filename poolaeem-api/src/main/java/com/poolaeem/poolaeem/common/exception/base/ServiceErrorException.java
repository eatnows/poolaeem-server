package com.poolaeem.poolaeem.common.exception.base;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public abstract class ServiceErrorException extends RuntimeException {

    protected ServiceErrorException() {
    }

    protected ServiceErrorException(String message) {
        super(message);
    }

    public abstract ApiResponseCode getApiResponseCode();
}
