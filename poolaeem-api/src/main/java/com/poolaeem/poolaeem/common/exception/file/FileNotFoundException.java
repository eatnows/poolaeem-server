package com.poolaeem.poolaeem.common.exception.file;

import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class FileNotFoundException extends ServiceException {

    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.FILE_NOT_FOUND;

    public FileNotFoundException() {
        super(RESPONSE_CODE.getMessage());
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
