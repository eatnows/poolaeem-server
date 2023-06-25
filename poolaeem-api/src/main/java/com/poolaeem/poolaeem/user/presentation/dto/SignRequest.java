package com.poolaeem.poolaeem.user.presentation.dto;

import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignRequest {

    private SignRequest() {
    }

    @Getter
    public static class SignInDto {
        private String code;
    }

    @Getter
    @NoArgsConstructor
    public static class SignUpTermsDto {
        @AssertTrue
        private Boolean isAgreeTerms;
        private OauthProvider oauthProvider;
        private String oauthId;

        public SignUpTermsDto(OauthProvider oauthProvider, String oauthId) {
            this.oauthProvider = oauthProvider;
            this.oauthId = oauthId;
        }

        public SignUpTermsDto(Boolean isAgreeTerms, OauthProvider oauthProvider, String oauthId) {
            this.isAgreeTerms = isAgreeTerms;
            this.oauthProvider = oauthProvider;
            this.oauthId = oauthId;
        }
    }
}
