package com.poolaeem.poolaeem.security.oauth2.handler;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    public LoginFailureHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
        }

        ApiResponseCode responseCode = createApiStatusCode(exception);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(responseCode.getHttpStatus().value());

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

//        try (OutputStream os = response.getOutputStream()){
//            objectMapper.writeValue(os, new ApiResponseDto<>(responseCode));
//            os.flush();
//        }
    }

    private ApiResponseCode createApiStatusCode(AuthenticationException exception) {
        String exceptionClassName = exception.getClass().getName();
        String exceptionName = exceptionClassName.substring(exceptionClassName.lastIndexOf(".") + 1);

        if ("UserNotSignedUpException".equalsIgnoreCase(exceptionName)) {
            return ApiResponseCode.USER_NOT_SIGNED_UP;
        }

        return ApiResponseCode.INVALID_SERVER_ERROR;
    }
}
