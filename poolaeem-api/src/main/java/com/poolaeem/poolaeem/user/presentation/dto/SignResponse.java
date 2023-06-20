package com.poolaeem.poolaeem.user.presentation.dto;

import lombok.Getter;

public class SignResponse {

    @Getter
    public static class RequiredTermsDto {
        private String oauthProvider;
        private String oauthId;

        public RequiredTermsDto(String oauthProvider, String oauthId) {
            this.oauthProvider = oauthProvider;
            this.oauthId = oauthId;
        }
    }
}
