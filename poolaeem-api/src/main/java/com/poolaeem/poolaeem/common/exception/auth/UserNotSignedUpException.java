package com.poolaeem.poolaeem.common.exception.auth;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import org.springframework.security.core.AuthenticationException;

public class UserNotSignedUpException extends AuthenticationException {

    public UserNotSignedUpException() {
        super(ApiResponseCode.USER_NOT_SIGNED_UP.getMessage());
    }
}
