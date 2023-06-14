package com.poolaeem.poolaeem.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiResponseDto<T> {
    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponseDto() {
        this(ApiResponseCode.SUCCESS);
    }

    public ApiResponseDto(ApiResponseCode responseCode) {
        this.code = responseCode.getCode();
    }

    private ApiResponseDto(int code) {
        this.code = code;
    }

    public ApiResponseDto(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
