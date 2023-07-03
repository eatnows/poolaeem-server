package com.poolaeem.poolaeem.common.exception.base;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public abstract class ServiceException extends RuntimeException {
    protected ServiceException() {
    }

    protected ServiceException(String message) {
        super(message);
    }

    public abstract ApiResponseCode getApiResponseCode();
}
