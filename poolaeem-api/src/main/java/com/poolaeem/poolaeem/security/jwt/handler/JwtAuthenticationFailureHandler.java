package com.poolaeem.poolaeem.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        try (OutputStream os = response.getOutputStream()){
            ApiResponseCode responseCode = createApiStatusCode(exception);
            objectMapper.writeValue(os, new ApiResponseDto<>(responseCode));
            os.flush();
        }
    }

    private ApiResponseCode createApiStatusCode(AuthenticationException exception) {
        String exceptionClassName = exception.getClass().getName();
        String exceptionName = exceptionClassName.substring(exceptionClassName.lastIndexOf(".") + 1);

        if ("ExpiredTokenException".equalsIgnoreCase(exceptionName)) {
            return ApiResponseCode.EXPIRED_TOKEN;
        }

        return ApiResponseCode.INVALID_TOKEN;
    }
}
