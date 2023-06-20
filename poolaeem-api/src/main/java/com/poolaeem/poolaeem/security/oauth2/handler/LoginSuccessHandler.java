package com.poolaeem.poolaeem.security.oauth2.handler;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
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
import java.util.Optional;

@Slf4j
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String DEFAULT_REDIRECT_URL = "https://poolaeem.com";
    public static final String SIGN_IN_SUCCESS_URL = DEFAULT_REDIRECT_URL + "";
    public static final String REQUIRED_TERMS_URL = DEFAULT_REDIRECT_URL + "";
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final LoginSuccessRedirect loginSuccessRedirect;

    public LoginSuccessHandler(JwtTokenUtil jwtTokenUtil, UserRepository userRepository, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository, LoginSuccessRedirect loginSuccessRedirect) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.loginSuccessRedirect = loginSuccessRedirect;
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
            requiredTermsAndConditions(request, response, getRequiredTermsUrl(oauthId, provider));
            return;
        }
        User user = userOptional.get();
        loginSuccessRedirect.redirectSignedIn(request, response, new GenerateTokenUser(user.getId(), user.getEmail(), user.getName(), null), SIGN_IN_SUCCESS_URL);
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

    private void requiredTermsAndConditions(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException {
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private String getRequiredTermsUrl(String oauthId, String oauthProvider) {
        return UriComponentsBuilder.fromUriString(REQUIRED_TERMS_URL)
                .queryParam("oauth-provider", oauthProvider)
                .queryParam("oauth-id", oauthId)
                .build().toUriString();
    }
}
