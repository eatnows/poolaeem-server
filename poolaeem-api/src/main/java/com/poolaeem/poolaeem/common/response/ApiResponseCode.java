package com.poolaeem.poolaeem.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseCode {

    SUCCESS(0, "완료", HttpStatus.OK),
    UNAUTHORIZED(10001, "인증되지 않았습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(10003, "요청 권한이 없습니다.", HttpStatus.FORBIDDEN),
    EXPIRED_TOKEN(10050, "토큰 유효시간이 만료 되었습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(10052, "유효한 토큰이 아닙니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_SIGNED_UP(10023, "회원가입 하지 않은 유저입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_BAD_REQUEST(10000, "회원가입 하지 않은 유저입니다.", HttpStatus.BAD_REQUEST),


    INVALID_SERVER_ERROR(20000, "알 수 없는 오류로 요청을 완료할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private int code;
    private String message;
    private HttpStatus httpStatus;

    ApiResponseCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
