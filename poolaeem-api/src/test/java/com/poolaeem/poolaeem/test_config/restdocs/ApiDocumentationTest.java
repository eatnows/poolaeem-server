package com.poolaeem.poolaeem.test_config.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.presentation.ProblemController;
import com.poolaeem.poolaeem.security.config.SecurityConfig;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAccessDeniedHandler;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationEntryPoint;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationFailureHandler;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationSuccessHandler;
import com.poolaeem.poolaeem.security.jwt.provider.JwtAuthenticationProvider;
import com.poolaeem.poolaeem.security.jwt.service.CustomUserDetailsService;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginFailureHandler;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessHandler;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessToken;
import com.poolaeem.poolaeem.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.poolaeem.poolaeem.security.oauth2.service.CustomOAuth2UserService;
import com.poolaeem.poolaeem.security.oauth2.service.CustomOidcUserService;
import com.poolaeem.poolaeem.solve.application.GradingService;
import com.poolaeem.poolaeem.solve.presentation.GradingController;
import com.poolaeem.poolaeem.user.application.ProfileInfoService;
import com.poolaeem.poolaeem.user.application.SignService;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.TermsVersion;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import com.poolaeem.poolaeem.user.presentation.ProfileInfoController;
import com.poolaeem.poolaeem.user.presentation.SignController;
import com.poolaeem.poolaeem.question.application.WorkbookService;
import com.poolaeem.poolaeem.question.presentation.WorkbookController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@WebMvcTest(
    controllers = {
            SignController.class,
            ProfileInfoController.class,
            WorkbookController.class,
            ProblemController.class,
            GradingController.class
    },
    properties = "spring.config.location=classpath:/application.yml"
)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@ImportAutoConfiguration({SecurityConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public abstract class ApiDocumentationTest {

    protected final String ACCESS_TOKEN = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IkF1dGhlbnRpY2F0aW9uIiwiY29kZSI6InVzZXItMSIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3OTUyMTE4LCJleHAiOjM2ODc5NTM5MTh9.QHwZ4Jppb5Bgp-oain43voHX5J4bkzt-7zTWKQOjFI77_H4k0HXYe79RjJopmvrOHX3YEFECfDJuTOgoMUqBQGFRmqSdEmUF-vFXOcjezvxw4NQzFTFkq430sNxrhK_RMsSbKTMnByZuN7Opj_RNGmGlDygX1dyqqqwxFZPn3fivl5TUM9VNhGACbCEGzxIYi7ACGNR2Kj61Qf343Dxyw37bXQ0D3a63Izxq8ThAzOtIyICnnF_ZYvn-3Y1nx9cpcxMdqwIGVFYBYSOOaCKnLJ7BUxEvy8l9tqwmGd2jCJWAAeSKNfcGmo4jpPrj5jJFvBoV2ynygZHHvkIXP0v_LQ";
    protected final String BEARER_ACCESS_TOKEN = "Bearer " + ACCESS_TOKEN;

    @BeforeEach
    protected void beforeEach() {
        given(userRepository.findDtoByUserIdAndIsDeletedFalse(anyString()))
                .willReturn(Optional.of(new UserVo(
                        "user-1",
                        "test@poolaee.com",
                        "풀내임",
                        UserRole.ROLE_USER,
                        OauthProvider.GOOGLE,
                        "1234567890",
                        null,
                        TermsVersion.V1
                )));
    }

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected JdbcTemplate jdbcTemplate;

    @SpyBean
    protected LoginSuccessToken loginSuccessToken;
    @SpyBean
    protected LoginSuccessHandler loginSuccessHandler;
    @SpyBean
    protected LoginFailureHandler loginFailureHandler;
    @SpyBean
    protected JwtAuthenticationProvider jwtAuthenticationProvider;
    @SpyBean
    protected JwtTokenUtil jwtTokenUtil;
    @SpyBean
    protected JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @SpyBean
    protected JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @SpyBean
    protected JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    @SpyBean
    protected JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    @SpyBean
    protected CustomOAuth2UserService customOAuth2UserService;
    @SpyBean
    protected CustomOidcUserService customOidcUserService;
    @MockBean
    protected UserRepository userRepository;
    @SpyBean
    protected HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    @SpyBean
    protected CustomUserDetailsService customUserDetailsService;

    @MockBean
    protected SignService signService;
    @MockBean
    protected ProfileInfoService profileInfoService;
    @MockBean
    protected WorkbookService workbookService;
    @MockBean
    protected ProblemService problemService;
    @MockBean
    protected GradingService gradingService;
}
