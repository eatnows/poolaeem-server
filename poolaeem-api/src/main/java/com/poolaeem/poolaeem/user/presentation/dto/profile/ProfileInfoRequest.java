package com.poolaeem.poolaeem.user.presentation.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileInfoRequest {

    @Getter
    @NoArgsConstructor
    public static class UserNameUpdateDto {
        @NotBlank
        @Length(min = 1, max = 30)
        private String userName;

        public UserNameUpdateDto(String userName) {
            this.userName = userName;
        }
    }
}
