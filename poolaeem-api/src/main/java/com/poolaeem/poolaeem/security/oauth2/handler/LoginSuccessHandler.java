package com.poolaeem.poolaeem.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.common.response.ApiResponseDto;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Slf4j
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final LoginSuccessToken loginSuccessToken;
    private final ObjectMapper objectMapper;

    public LoginSuccessHandler(JwtTokenUtil jwtTokenUtil, UserRepository userRepository, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, LoginSuccessToken loginSuccessToken, ObjectMapper objectMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
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

        Optional<User> userOptional = userRepository.findByEmailAndOauthProviderAndOauthIdAndIsDeletedFalse(email, oauthProvider, oauthId);

        if (isUserNotSignedUp(userOptional)) {
            try (OutputStream os = response.getOutputStream()){
                objectMapper.writeValue(os, new ApiResponseDto<>(ApiResponseCode.USER_NOT_SIGNED_UP));
                os.flush();
            }
            return;
        }

        User user = userOptional.get();
        loginSuccessToken.addTokenInResponse(response, new GenerateTokenUser(user.getId(), user.getEmail(), user.getName(), null));

        try (OutputStream os = response.getOutputStream()){
            objectMapper.writeValue(os, new ApiResponseDto<>(ApiResponseCode.SUCCESS));
            os.flush();
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    private boolean isUserNotSignedUp(Optional<User> userOptional) {
        return userOptional.isEmpty();
    }



    protected final void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

//    private void requiredTermsAndConditions(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException {
//        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//    }

//    private String getRequiredTermsUrl(String oauthId, String oauthProvider) {
//        return UriComponentsBuilder.fromUriString(REQUIRED_TERMS_URL)
//                .queryParam("oauth-provider", oauthProvider)
//                .queryParam("oauth-id", oauthId)
//                .build().toUriString();
//    }
}
