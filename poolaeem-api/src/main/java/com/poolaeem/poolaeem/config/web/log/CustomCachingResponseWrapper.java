package com.poolaeem.poolaeem.config.web.log;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

public class CustomCachingResponseWrapper extends ContentCachingResponseWrapper {

    public CustomCachingResponseWrapper(HttpServletResponse response) {
        super(response);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}
