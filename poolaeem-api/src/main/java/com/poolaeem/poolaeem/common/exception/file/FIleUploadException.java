package com.poolaeem.poolaeem.common.exception.file;

import com.poolaeem.poolaeem.common.exception.base.ServiceErrorException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class FIleUploadException extends ServiceErrorException {

    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.FILE_UPLOAD_ERROR;

    public FIleUploadException() {
        super(RESPONSE_CODE.getMessage());
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
