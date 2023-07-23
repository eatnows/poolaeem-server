package com.poolaeem.poolaeem.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poolaeem.poolaeem.common.exception.auth.UserNotSignedUpException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import com.poolaeem.poolaeem.user.presentation.dto.auth.SignResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Slf4j
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final LoginSuccessToken loginSuccessToken;
    private final ObjectMapper objectMapper;

    public LoginSuccessHandler(UserRepository userRepository, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, LoginSuccessToken loginSuccessToken, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.loginSuccessToken = loginSuccessToken;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        clearAuthenticationAttributes(request, response);
        String oauthId = ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttribute("sub");
        String email = ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttribute("email");
        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        OauthProvider oauthProvider = OauthProvider.valueOf(provider.toUpperCase());

        Optional<User> userOptional = userRepository.findByOauthProviderAndOauthIdAndIsDeletedFalse(oauthProvider, oauthId);

        if (isUserNotSignedUp(userOptional, email)) {
            requestAgreementForSignUpTerms(response, oauthId, email, provider);
            return;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());

        User user = userOptional.get();
        loginSuccessToken.addTokenInResponse(response, new GenerateTokenUser(user.getId()));

        try (OutputStream os = response.getOutputStream()){
            objectMapper.writeValue(os, new ApiResponseDto<>(ApiResponseCode.SUCCESS));
            os.flush();
        }
    }

    private void requestAgreementForSignUpTerms(HttpServletResponse response, String oauthId, String email, String provider) throws IOException {
        try (OutputStream os = response.getOutputStream()){
            objectMapper.writeValue(os, new ApiResponseDto<>(ApiResponseCode.USER_NOT_SIGNED_UP, new SignResponse.RequiredTermsDto(OauthProvider.valueOf(provider.toUpperCase()), oauthId, email)));
            os.flush();
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    private boolean isUserNotSignedUp(Optional<User> userOptional, String email) {
        if (userOptional.isEmpty()) {
            return true;
        }
        if (!userOptional.get().getEmail().equals(email)) {
            throw new UserNotSignedUpException();
        }

        return false;
    }



    protected final void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
