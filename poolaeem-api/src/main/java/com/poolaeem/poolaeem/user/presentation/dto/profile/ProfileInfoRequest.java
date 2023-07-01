package com.poolaeem.poolaeem.user.presentation.dto.profile;

import com.poolaeem.poolaeem.user.domain.validation.UserValidation;
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
        @Length(min = UserValidation.USER_NAME_MIN_LENGTH, max = UserValidation.USER_NAME_MAX_LENGTH)
        private String userName;

        public UserNameUpdateDto(String userName) {
            this.userName = userName;
        }
    }
}
