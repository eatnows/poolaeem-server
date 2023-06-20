package com.poolaeem.poolaeem.user.presentation.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;

public class SignRequest {

    @Getter
    public static class SignInDto {
        private String code;
    }

    @Getter
    public static class SignUpTermsDto {
        @AssertTrue
        private Boolean isAgreeTerms;
        private String oauthProvider;
        private String oauthId;

        public SignUpTermsDto(String oauthProvider, String oauthId) {
            this.oauthProvider = oauthProvider;
            this.oauthId = oauthId;
        }
    }
}
