package com.poolaeem.poolaeem.common.exception.handler;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(1)
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiResponseDto<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        try {
            ApiResponseCode apiResponseCode = ApiResponseCode.BAD_REQUEST_DATA;
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(apiResponseCode, e.getMessage());

            log.info("> method argument not valid exception: ", e);

            return new ResponseEntity<>(responseDto, apiResponseCode.getHttpStatus());
        } catch (Exception ex) {
            log.error("> method argument not valid exception handler error: ", ex);

            ApiResponseCode responseCode = ApiResponseCode.INVALID_SERVER_ERROR;
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, responseCode.getMessage());

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        }
    }
    @ExceptionHandler(ValidationException.class)
    private ResponseEntity<ApiResponseDto<String>> handleValidationException(ValidationException e) {
        try {
            ApiResponseCode apiResponseCode = ApiResponseCode.BAD_REQUEST_DATA;
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(apiResponseCode, apiResponseCode.getMessage());

            log.info("> validation exception: ", e);

            return new ResponseEntity<>(responseDto, apiResponseCode.getHttpStatus());
        } catch (Exception ex) {
            log.error("> validation exception handler error: ", ex);

            ApiResponseCode responseCode = ApiResponseCode.INVALID_SERVER_ERROR;
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, responseCode.getMessage());

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        }
    }
}
