package com.poolaeem.poolaeem.common.aop;

import com.poolaeem.poolaeem.common.annotation.LoggedInUserOnly;
import com.poolaeem.poolaeem.common.exception.auth.UnAuthorizationException;
import com.poolaeem.poolaeem.security.jwt.token.CustomUserDetail;
import com.poolaeem.poolaeem.security.jwt.token.NonLoggedInUserDetail;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoggedInOnlyValidAspectTest {
    private final LoggedInOnlyValidAspect loggedInOnlyValidAspect = new LoggedInOnlyValidAspect();
    private LoggedInOnlyValidTestController testController;

    @BeforeEach
    void setUp() {
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(new LoggedInOnlyValidTestController());
        aspectJProxyFactory.addAspect(loggedInOnlyValidAspect);

        DefaultAopProxyFactory proxyFactory = new DefaultAopProxyFactory();
        AopProxy aopProxy = proxyFactory.createAopProxy(aspectJProxyFactory);

        testController = (LoggedInOnlyValidTestController) aopProxy.getProxy();
    }

    @Test
    @DisplayName("로그인을 하지 않으면 LoggedInUserOnly 애너테이션이 있는 메소드를 요청할 수 없다.")
    void testLoggedInUserOnlyForNotLoggedIn() {
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("key", new NonLoggedInUserDetail(), List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));

        assertThatThrownBy(() -> testController.loggedInUserOnlyMethod())
                .isInstanceOf(UnAuthorizationException.class);
    }

    @Test
    @DisplayName("로그인을 하여 인증객체가 생성된 유저는 LoggedInUserOnly 애너테이션이 있는 메소드를 요청할 수 있다.")
    void testLoggedInUserOnly() {
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(
                "key",
                new CustomUserDetail(new UserVo("user-id", "email", "name", UserRole.ROLE_USER, null, null, null, null)),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))));

        assertThat(testController.loggedInUserOnlyMethod()).isTrue();
    }

    @Test
    @DisplayName("LoggedInUserOnly 애너테이션이 없는 메소드는 비로그인도 요청할 수 있다.")
    void testNotLoggedInUserOnly() {
        assertThat(testController.notLoggedInUserOnlyMethod()).isTrue();
    }

    private static class LoggedInOnlyValidTestController {
        public LoggedInOnlyValidTestController() {
        }

        @LoggedInUserOnly
        public boolean loggedInUserOnlyMethod() {
            return true;
        }

        public boolean notLoggedInUserOnlyMethod() {
            return true;
        }
    }
}