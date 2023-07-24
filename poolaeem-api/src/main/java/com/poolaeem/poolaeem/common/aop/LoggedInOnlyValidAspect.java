package com.poolaeem.poolaeem.common.aop;

import com.poolaeem.poolaeem.common.exception.auth.UnAuthorizationException;
import com.poolaeem.poolaeem.security.jwt.token.NonLoggedInUserDetail;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggedInOnlyValidAspect {
    @Before(value = "@annotation(com.poolaeem.poolaeem.common.annotation.LoggedInUserOnly)")
    public void validLoggedIn(JoinPoint joinPoint) {
        UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetail == null || userDetail instanceof NonLoggedInUserDetail) {
            throw new UnAuthorizationException();
        }
    }
}
