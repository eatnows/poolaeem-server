package com.poolaeem.poolaeem.common.jwt;

import com.poolaeem.poolaeem.common.exception.jwt.InvalidTokenException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class HeaderTokenExtractor {

    public final String HEADER_PREFIX = "Bearer ";

    public String extract(String headerToken) {
        if (!StringUtils.hasLength(headerToken) || headerToken.length() <= HEADER_PREFIX.length()) {
            throw new InvalidTokenException();
        }

        return headerToken.substring(HEADER_PREFIX.length());
    }

    public String getBearerToken(HttpHeaders headers) {
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
