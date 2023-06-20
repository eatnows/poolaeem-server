package com.poolaeem.poolaeem.security.oauth2.handler;

import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.security.oauth2.model.GenerateTokenUser;
import com.poolaeem.poolaeem.security.oauth2.repository.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static com.poolaeem.poolaeem.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class LoginSuccessRedirect {

    private final JwtTokenUtil jwtTokenUtil;

    public LoginSuccessRedirect(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public void redirectSignedIn(HttpServletRequest request, HttpServletResponse response, GenerateTokenUser generateTokenUser, String targetUrl) throws IOException {

        String accessToken = jwtTokenUtil.generateAccessToken(generateTokenUser);
        String refreshToken = jwtTokenUtil.generateRefreshToken(generateTokenUser);
//
        response.addHeader("ac", accessToken);
        response.addHeader("rf", refreshToken);

        ApiResponseCode responseCode = ApiResponseCode.SUCCESS;
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.CREATED.value());

        String url = getSuccessUrl(request, response, accessToken, refreshToken, targetUrl);
        DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();
        defaultRedirectStrategy.sendRedirect(request, response, url);
    }

    private String getSuccessUrl(HttpServletRequest request, HttpServletResponse response, String accessToken, String refreshToken, String url) {
        int cookieMaxAge = (int) (1000 * 60 * 60 * 24 * 15) / 60;

        CookieUtils.deleteCookie(request, response, refreshToken);
        CookieUtils.addCookie(response, "refresh_token", refreshToken, cookieMaxAge);

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(url);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access-token", accessToken)
                .queryParam("refresh-token", refreshToken)
                .build().toUriString();
    }
}
