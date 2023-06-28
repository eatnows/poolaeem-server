package com.poolaeem.poolaeem.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseCode {

    SUCCESS(0, "완료", HttpStatus.OK),
    BAD_REQUEST(10000, "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(10001, "인증되지 않았습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(10003, "요청 권한이 없습니다.", HttpStatus.FORBIDDEN),
    BAD_REQUEST_DATA(10008, "잘못된 요청 데이터입니다.", HttpStatus.BAD_REQUEST),
    REQUEST_VALIDATION(10009, "요청 데이터가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_SIGNED_UP(10023, "회원가입 하지 않은 유저입니다.", HttpStatus.UNAUTHORIZED),
    DUPLICATE_SIGNED_UP(10024, "이미 회원가입한 유저입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(10050, "토큰 유효시간이 만료 되었습니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(10052, "유효한 토큰이 아닙니다.", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(10064, "파일이 존재하지 않습니다.", HttpStatus.NOT_FOUND),


    INVALID_SERVER_ERROR(20000, "알 수 없는 오류로 요청을 완료할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ENCRYPT_ERROR(20012, "암호화 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DECRYPT_ERROR(20012, "복호화 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_ERROR(20016, "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
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
