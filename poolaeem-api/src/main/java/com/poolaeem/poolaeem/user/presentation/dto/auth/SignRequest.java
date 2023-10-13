package com.poolaeem.poolaeem.user.presentation.dto.auth;

import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignRequest {

    private SignRequest() {
    }

    public record SignInDto (
            String code
    ){
    }

    public record SignUpTermsDto(
            @AssertTrue
            Boolean isAgreeTerms,
            @NotNull
            OauthProvider oauthProvider,
            @NotBlank
            String oauthId,
            @Email
            @NotNull
            String email
    ) {
        public SignUpTermsDto(OauthProvider oauthProvider, String oauthId, String email) {
            this(null, oauthProvider, oauthId, email);
        }
    }
}
