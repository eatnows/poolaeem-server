package com.poolaeem.poolaeem.common.exception.handler;

import com.poolaeem.poolaeem.common.exception.jwt.ExpiredTokenException;
import com.poolaeem.poolaeem.common.exception.jwt.InvalidTokenException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(3)
public class JwtValidationExceptionHandler {
    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<ApiResponseDto<String>> handleInvalidTokenException(InvalidTokenException e) {
        try {
            ApiResponseCode responseCode = e.getApiResponseCode();
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, e.getMessage());

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        } catch (Exception ex) {
            log.error("> InvalidToken exception handler error: ", ex);

            ApiResponseCode responseCode = ApiResponseCode.INVALID_SERVER_ERROR;
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, responseCode.getMessage());

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        }
    }

    @ExceptionHandler(ExpiredTokenException.class)
    private ResponseEntity<ApiResponseDto<String>> handleExpiredTokenException(ExpiredTokenException e) {
        try {
            ApiResponseCode responseCode = e.getApiResponseCode();
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, e.getMessage());

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        } catch (Exception ex) {
            log.error("> ExpiredToken exception handler error: ", ex);

            ApiResponseCode responseCode = ApiResponseCode.INVALID_SERVER_ERROR;
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, responseCode.getMessage());

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        }
    }
}
