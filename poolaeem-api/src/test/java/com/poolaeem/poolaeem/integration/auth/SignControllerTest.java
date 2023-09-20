package com.poolaeem.poolaeem.integration.auth;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.user.domain.entity.LoggedInHistory;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.domain.service.auth.SignUpOAuth2UserService;
import com.poolaeem.poolaeem.user.infra.repository.LoggedInHistoryRepository;
import com.poolaeem.poolaeem.user.infra.repository.UserRepository;
import com.poolaeem.poolaeem.user.presentation.dto.auth.SignRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("통합: 계정인증 테스트")
@Sql(scripts = {
        "classpath:/sql/user/loggedInHistory.sql"
})
class SignControllerTest extends BaseIntegrationTest {

    private final String AGREE_SIGN_UP_TERMS = "/api/signup/terms";
    private final String GENERATE_ACCESS_TOKEN_BY_REFRESH_TOKEN = "/api/access-token/refresh";
    private final String BEARER_REFRESH_TOKEN = "Bearer eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.abGtDWHblKjihChHShvBKcKo1uu0CkLPInmDduFNPeYMUduZICgvaYzBlvvTK8brekrSC3ahlH4ROnCTxVwFN-IqxUxdIxNS-FXAQcoZBWRnWRHeLK56ZcwKo1RmCbzD4d1hEP907fb_zjAPrgrk6GgWO-L-Yq0z0K1Ntgdgz5M7I4gWRayeI13TBdaR6DqcAfrNCqK3Gg_0cxtgwN7cGvVBVPoG1jufXPt1uxynwfMHzTXY7UtvKRjDO3g0vyTBF52abK85eBMmvKIaGMEKOKWuN75ZYFS8xIFluGOouZsoLII_jTVIV1xMcWHe0a3WNxV5sh-EugLrVsJVfIJofQ";

    @MockBean
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    @MockBean
    private SignUpOAuth2UserService signUpOAuth2UserService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoggedInHistoryRepository loggedInHistoryRepository;

    @Test
    @DisplayName("이용약관 동의를 해야만 OAuth2 회원가입을 할 수 있다")
    void agreeSignUpTerms() throws Exception {
        given(oAuth2AuthorizedClientService.loadAuthorizedClient(anyString(), anyString()))
                .willReturn(new OAuth2AuthorizedClient(
                        ClientRegistration.withRegistrationId("google")
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .clientId("oauth2-client-id")
                                .redirectUri("https://poolaeem.com/oauth2/redirect")
                                .authorizationUri("https://poolaeem.com/oauth2/authorization")
                                .tokenUri("https://poolaeem.com/oauth2/tokenuri")
                                .build(),
                        "1234567890",
                        new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                                "access-token",
                                Instant.now(Clock.systemUTC()),
                                Instant.now().plusSeconds(3600)
                        )
                ));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "1234567890");
        attributes.put("email", "test@poolaeem.com");
        attributes.put("name", "풀내임");

        given(signUpOAuth2UserService.loadUser(any()))
                .willReturn(new DefaultOAuth2User(List.of(new SimpleGrantedAuthority("ROLE_USER")), attributes, "name"));

        ResultActions result = this.mockMvc.perform(
                post(AGREE_SIGN_UP_TERMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignRequest.SignUpTermsDto(
                                true, OauthProvider.GOOGLE, "1234567890", "test@poolaeem.com"
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(header().exists("access-token"))
                .andExpect(header().exists("refresh-token"));
    }

    @Test
    @DisplayName("이용약관 동의를 하지 않으면 OAuth2 회원가입을 할 수 없다")
    void disagreeSignUpTerms() throws Exception {
        ResultActions result = this.mockMvc.perform(
                post(AGREE_SIGN_UP_TERMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignRequest.SignUpTermsDto(
                                false, OauthProvider.GOOGLE, "1234567890", "test@poolaeem.com"
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("이메일을 보내지 않으면 OAuth2 회원가입을 할 수 없다")
    void blockSignUpIfEmailDoesNotExist() throws Exception {
        ResultActions result = this.mockMvc.perform(
                post(AGREE_SIGN_UP_TERMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignRequest.SignUpTermsDto(
                                true, OauthProvider.GOOGLE, "1234567890", null
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.BAD_REQUEST_DATA.getCode())));
    }

    @Test
    @DisplayName("요청한 이메일과 실제 이메일이 다르면 OAuth2 회원가입을 할 수 없다")
    void blockSignUpOnEmailMismatch() throws Exception {
        given(oAuth2AuthorizedClientService.loadAuthorizedClient(anyString(), anyString()))
                .willReturn(new OAuth2AuthorizedClient(
                        ClientRegistration.withRegistrationId("google")
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .clientId("oauth2-client-id")
                                .redirectUri("https://poolaeem.com/oauth2/redirect")
                                .authorizationUri("https://poolaeem.com/oauth2/authorization")
                                .tokenUri("https://poolaeem.com/oauth2/tokenuri")
                                .build(),
                        "1234567890",
                        new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                                "access-token",
                                Instant.now(Clock.systemUTC()),
                                Instant.now().plusSeconds(3600)
                        )
                ));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "1234567890");
        attributes.put("email", "test@poolaeem.com");
        attributes.put("name", "풀내임");

        given(signUpOAuth2UserService.loadUser(any()))
                .willReturn(new DefaultOAuth2User(List.of(new SimpleGrantedAuthority("ROLE_USER")), attributes, "name"));
        ResultActions result = this.mockMvc.perform(
                post(AGREE_SIGN_UP_TERMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignRequest.SignUpTermsDto(
                                true, OauthProvider.GOOGLE, "1234567890", "poolaeem@poolaeem.com"
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest())
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(BadRequestDataException.class))
                .andExpect(jsonPath("$.code", is(ApiResponseCode.BAD_REQUEST_DATA.getCode())));
    }

    @Test
    @DisplayName("리프레쉬 토큰으로 액세스 토큰을 발급받을 수 있다.")
    void testGenerateAccessTokenByRefreshToken() throws Exception {
        ResultActions result = this.mockMvc.perform(
                post(GENERATE_ACCESS_TOKEN_BY_REFRESH_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Refresh", BEARER_REFRESH_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(header().exists("access-token"))
                .andExpect(jsonPath("$.code", is(0)));
    }

    @Test
    @DisplayName("회원가입을 한 후 로그인을 하면 로그인 기록이 수집된다.")
    void testSaveLoggedInHistoryAfterSignUp() throws Exception {
        given(oAuth2AuthorizedClientService.loadAuthorizedClient(anyString(), anyString()))
                .willReturn(new OAuth2AuthorizedClient(
                        ClientRegistration.withRegistrationId("google")
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .clientId("oauth2-client-id")
                                .redirectUri("https://poolaeem.com/oauth2/redirect")
                                .authorizationUri("https://poolaeem.com/oauth2/authorization")
                                .tokenUri("https://poolaeem.com/oauth2/tokenuri")
                                .build(),
                        "1234567890",
                        new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                                "access-token",
                                Instant.now(Clock.systemUTC()),
                                Instant.now().plusSeconds(3600)
                        )
                ));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "1234567890");
        attributes.put("email", "test@poolaeem.com");
        attributes.put("name", "풀내임");

        given(signUpOAuth2UserService.loadUser(any()))
                .willReturn(new DefaultOAuth2User(List.of(new SimpleGrantedAuthority("ROLE_USER")), attributes, "name"));

        ResultActions result = this.mockMvc.perform(
                post(AGREE_SIGN_UP_TERMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignRequest.SignUpTermsDto(
                                true, OauthProvider.GOOGLE, "1234567890", "test@poolaeem.com"
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        User user = userRepository.findAll().stream().sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt())).findFirst().get();
        List<LoggedInHistory> histories = loggedInHistoryRepository.findAll();

        assertThat(user.getLastLoggedAt()).isNotNull();
        assertThat(histories).hasSize(1);
    }
}