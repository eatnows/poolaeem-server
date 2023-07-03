package com.poolaeem.poolaeem.user.domain.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserValidation {
    public static final int USER_NAME_MIN_LENGTH = 1;
    public static final int USER_NAME_MAX_LENGTH = 30;
}
