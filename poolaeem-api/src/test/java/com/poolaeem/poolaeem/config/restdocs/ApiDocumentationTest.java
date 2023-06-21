package com.poolaeem.poolaeem.config.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poolaeem.poolaeem.common.jwt.HeaderTokenExtractor;
import com.poolaeem.poolaeem.common.jwt.JwtTokenUtil;
import com.poolaeem.poolaeem.security.config.SecurityConfig;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAccessDeniedHandler;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationEntryPoint;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationFailureHandler;
import com.poolaeem.poolaeem.security.jwt.handler.JwtAuthenticationSuccessHandler;
import com.poolaeem.poolaeem.security.jwt.provider.JwtAuthenticationProvider;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginFailureHandler;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessHandler;
import com.poolaeem.poolaeem.security.oauth2.handler.LoginSuccessToken;
import com.poolaeem.poolaeem.security.oauth2.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.poolaeem.poolaeem.security.oauth2.service.CustomOAuth2UserService;
import com.poolaeem.poolaeem.security.oauth2.service.CustomOidcUserService;
import com.poolaeem.poolaeem.user.application.SignService;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import com.poolaeem.poolaeem.user.presentation.SignController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {
            SignController.class
    },
    properties = "spring.config.location=classpath:/application.yml"
)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@ImportAutoConfiguration({SecurityConfig.class})
public abstract class ApiDocumentationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected JdbcTemplate jdbcTemplate;

    @MockBean
    private LoginSuccessToken loginSuccessToken;
    @SpyBean
    private LoginSuccessHandler loginSuccessHandler;
    @SpyBean
    private LoginFailureHandler loginFailureHandler;
    @SpyBean
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @SpyBean
    private JwtTokenUtil jwtTokenUtil;
    @SpyBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @SpyBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @SpyBean
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    @SpyBean
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    @SpyBean
    private CustomOAuth2UserService customOAuth2UserService;
    @SpyBean
    private CustomOidcUserService customOidcUserService;
    @MockBean
    private UserRepository userRepository;
    @SpyBean
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;



    @MockBean
    private SignService signService;
}
