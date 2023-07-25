package com.poolaeem.poolaeem.user.presentation.dto.profile;

import com.poolaeem.poolaeem.user.domain.validation.UserValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileInfoRequest {

    @Getter
    @NoArgsConstructor
    public static class UserNameUpdateDto {
        @NotBlank(message = UserValidation.Message.USER_NAME_VALID)
        @Length(min = UserValidation.USER_NAME_MIN_LENGTH, max = UserValidation.USER_NAME_MAX_LENGTH, message = UserValidation.Message.USER_NAME_VALID)
        private String userName;

        public UserNameUpdateDto(String userName) {
            this.userName = userName;
        }
    }

    @Getter
    public static class ProfileImageUpdateDto {
        private MultipartFile file;

        public void setFile(MultipartFile file) {
            this.file = file;
        }
    }
}
