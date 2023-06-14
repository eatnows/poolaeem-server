package com.poolaeem.poolaeem.common.response;

public enum ApiResponseCode {

    SUCCESS(0, "완료"),
    UNAUTHORIZED(10001, "인증되지 않았습니다."),
    FORBIDDEN(10003, "요청 권한이 없습니다."),
    EXPIRED_TOKEN(10050, "토큰 유효시간이 만료 되었습니다."),
    INVALID_TOKEN(10052, "유효한 토큰이 아닙니다."),
    ;

    private int code;
    private String message;

    ApiResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
