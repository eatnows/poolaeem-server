package com.poolaeem.poolaeem.user.presentation.dto.auth;

import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import lombok.Getter;

public class SignResponse {
    private SignResponse() {
    }

    @Getter
    public static class RequiredTermsDto {
        private OauthProvider oauthProvider;
        private String oauthId;
        private String email;

        public RequiredTermsDto(OauthProvider oauthProvider, String oauthId, String email) {
            this.oauthProvider = oauthProvider;
            this.oauthId = oauthId;
            this.email = email;
        }
    }
}
