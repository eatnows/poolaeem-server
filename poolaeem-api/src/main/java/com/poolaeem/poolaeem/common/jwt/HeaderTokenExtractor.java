package com.poolaeem.poolaeem.common.jwt;

import com.poolaeem.poolaeem.common.exception.jwt.InvalidTokenException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class HeaderTokenExtractor {
    public static final String HEADER_PREFIX = "Bearer ";

    public static String extract(String headerToken) {
        if (!StringUtils.hasLength(headerToken) || headerToken.length() <= HEADER_PREFIX.length()) {
            throw new InvalidTokenException();
        }

        return headerToken.substring(HEADER_PREFIX.length());
    }

    public static String getBearerToken(HttpHeaders headers) {
        String bearerToken;
        try {
            bearerToken = headers.get("Authorization").get(0);
        } catch (NullPointerException e) {
            throw new InvalidTokenException();
        }
        return bearerToken;
    }

    public String getJwtToken(HttpHeaders headers) {
        return extract(getBearerToken(headers));
    }
}
