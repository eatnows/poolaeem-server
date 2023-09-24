package com.poolaeem.poolaeem.user.domain.dto;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public record RequestClient(
    String clientIp,
    String userAgent
) {
    public RequestClient(HttpServletRequest request) {
        this(Optional.ofNullable(request.getHeader("x-forwarded-for")).orElse(""),
                Optional.ofNullable(request.getHeader("user-agent")).orElse(""));
    }
}
