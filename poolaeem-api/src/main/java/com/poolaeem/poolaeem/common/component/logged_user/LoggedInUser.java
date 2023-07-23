package com.poolaeem.poolaeem.common.component.logged_user;

import com.poolaeem.poolaeem.security.jwt.token.CustomUserDetail;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggedInUser {
    public static String getUserId() {
        UserVo user = ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        return user.getId();
    }
}
