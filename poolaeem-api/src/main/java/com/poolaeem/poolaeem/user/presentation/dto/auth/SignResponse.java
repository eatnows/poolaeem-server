package com.poolaeem.poolaeem.user.presentation.dto.auth;

import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;

public class SignResponse {
    private SignResponse() {
    }

    public record RequiredTermsDto(
            OauthProvider oauthProvider,
            String oauthId,
            String email
    ) {
    }
}
