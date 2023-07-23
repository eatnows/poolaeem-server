package com.poolaeem.poolaeem.user.domain.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserValidation {
    public static final int USER_NAME_MIN_LENGTH = 1;
    public static final int USER_NAME_MAX_LENGTH = 30;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Message {
        public static final String NOT_SIGNED_UP_USER = "회원가입된 유저가 아닙니다.";
        public static final String DUPLICATE_SIGN_UP_USER = "이미 가입된 유저입니다.";
        public static final String USER_NOT_FOUND = "유저를 찾을 수 없습니다.";
        public static final String USER_NAME_MIN_LENGTH = "이름은 " + UserValidation.USER_NAME_MIN_LENGTH + "자 이상이어야 합니다.";
        public static final String USER_NAME_MAX_LENGTH = "이름은 " + UserValidation.USER_NAME_MAX_LENGTH + "자 이하이어야 합니다.";
        public static final String USER_NAME_VALID = "이름을 확인해주세요.";
        public static final String PROFILE_IMAGE_NOT_FOUND = "프로필 이미지를 찾을 수 없습니다.";
    }
}
