package com.poolaeem.poolaeem.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiResponseDto<T> {
    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private ApiResponseDto() {
        this(ApiResponseCode.SUCCESS);
    }

    public ApiResponseDto(ApiResponseCode responseCode) {
        this.code = responseCode.getCode();
    }

    private ApiResponseDto(int code) {
        this.code = code;
    }

    private ApiResponseDto(ApiResponseCode code, T data) {
        this.code = code.getCode();
        this.data = data;
    }

    public static ApiResponseDto OK() {
        return new ApiResponseDto(ApiResponseCode.SUCCESS);
    }

    public static <T> ApiResponseDto OK(T data) {
        return new ApiResponseDto(ApiResponseCode.SUCCESS, data);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
