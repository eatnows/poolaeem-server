package com.poolaeem.poolaeem.common.exception.workbook;


import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;

public class WorkbookNotFoundException extends ServiceException {

    private static final ApiResponseCode RESPONSE_CODE = ApiResponseCode.WORKBOOK_NOT_FOUND;
    public WorkbookNotFoundException() {
        super(RESPONSE_CODE.getMessage());
    }

    public WorkbookNotFoundException(String message) {
        super(message);
    }

    @Override
    public ApiResponseCode getApiResponseCode() {
        return RESPONSE_CODE;
    }
}
