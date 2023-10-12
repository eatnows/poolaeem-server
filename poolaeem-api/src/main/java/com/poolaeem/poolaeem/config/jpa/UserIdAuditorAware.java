package com.poolaeem.poolaeem.config.jpa;

import com.poolaeem.poolaeem.common.exception.auth.UnAuthorizationException;
import com.poolaeem.poolaeem.security.jwt.token.CustomUserDetail;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UserIdAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = Optional.ofNullable(
                        SecurityContextHolder.getContext().getAuthentication())
                .orElseThrow(UnAuthorizationException::new);

        if (!authentication.isAuthenticated()
                || authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ANONYMOUS")) {
            return Optional.empty();
        }

        return Optional.ofNullable(((CustomUserDetail) authentication.getPrincipal()))
                .map(CustomUserDetail::getUser)
                .map(UserVo::id);
    }
}
