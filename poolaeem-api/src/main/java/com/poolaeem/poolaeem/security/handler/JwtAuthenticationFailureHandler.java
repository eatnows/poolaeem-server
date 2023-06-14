package com.poolaeem.poolaeem.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        // TODO API response template 생성하여 수정
        try (OutputStream os = response.getOutputStream()){
            objectMapper.writeValue(os, new String());
            os.flush();
        }
    }

//    private ApiResponseCode createApiStatusCode(AuthenticationException exception) {
//        String exceptionClassName = exception.getClass().getName();
//        String exceptionName = exceptionClassName.substring(exceptionClassName.lastIndexOf(".") + 1);
//
//        if ("ExpiredTokenException".equalsIgnoreCase(exceptionName)) {
//            return ApiResponseCode.ERROR_EXPIRED_JWT_TOKEN;
//        }
//
//        if ("JwtIssuerException".equalsIgnoreCase(exceptionName)) {
//            return ApiResponseCode.ERROR_ISSUER_JWT_TOKEN;
//        }
//
//
//        return ApiResponseCode.ERROR_INVALID_JWT_TOKEN;
//    }
}
