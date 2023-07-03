package com.poolaeem.poolaeem.user.presentation.dto.auth;

import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        @NotNull
        private OauthProvider oauthProvider;
        @NotBlank
        private String oauthId;
        @Email
        @NotNull
        private String email;

        public SignUpTermsDto(OauthProvider oauthProvider, String oauthId, String email) {
            this.oauthProvider = oauthProvider;
            this.oauthId = oauthId;
            this.email = email;
        }

        public SignUpTermsDto(Boolean isAgreeTerms, OauthProvider oauthProvider, String oauthId, String email) {
            this.isAgreeTerms = isAgreeTerms;
            this.oauthProvider = oauthProvider;
            this.oauthId = oauthId;
            this.email = email;
        }
    }
}
