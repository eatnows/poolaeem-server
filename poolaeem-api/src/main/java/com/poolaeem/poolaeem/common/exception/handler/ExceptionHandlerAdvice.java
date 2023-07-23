package com.poolaeem.poolaeem.common.exception.handler;

import com.poolaeem.poolaeem.common.exception.base.ServiceErrorException;
import com.poolaeem.poolaeem.common.exception.base.ServiceException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(3)
public class ExceptionHandlerAdvice {
    @ExceptionHandler(ServiceException.class)
    private ResponseEntity<ApiResponseDto<String>> handleServiceException(ServiceException e) {
        try {
            ApiResponseCode responseCode = e.getApiResponseCode();
            String message = responseCode.getMessage();
            if (StringUtils.hasText(e.getMessage())) {
                message = e.getMessage();
            }
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, message);

            log.info("> service exception: ", e);

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        } catch (Exception ex) {
            log.error("> service exception handler error: ", ex);

            ApiResponseCode responseCode = ApiResponseCode.INVALID_SERVER_ERROR;
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, responseCode.getMessage());

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        }
    }

    @ExceptionHandler(ServiceErrorException.class)
    private ResponseEntity<ApiResponseDto<String>> handleServiceErrorException(ServiceErrorException e) {
        try {
            ApiResponseCode responseCode = e.getApiResponseCode();
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, responseCode.getMessage());

            log.info("> service error exception: ", e);

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        } catch (Exception ex) {
            log.error("> service exception handler error: ", ex);

            ApiResponseCode responseCode = ApiResponseCode.INVALID_SERVER_ERROR;
            ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, responseCode.getMessage());

            return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
        }
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiResponseDto<String>> handleException(Exception e) {
        log.error("> error exception: ", e);

        ApiResponseCode responseCode = ApiResponseCode.INVALID_SERVER_ERROR;
        ApiResponseDto<String> responseDto = new ApiResponseDto<>(responseCode, responseCode.getMessage());

        return new ResponseEntity<>(responseDto, responseCode.getHttpStatus());
    }
}
