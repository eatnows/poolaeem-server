package com.poolaeem.poolaeem.user.presentation;

import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.test_config.restdocs.DocumentLinkGenerator;
import com.poolaeem.poolaeem.user.domain.entity.OauthProvider;
import com.poolaeem.poolaeem.user.domain.entity.TermsVersion;
import com.poolaeem.poolaeem.user.domain.entity.User;
import com.poolaeem.poolaeem.user.presentation.dto.auth.SignRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: OAuth2 로그인 테스트")
class SignControllerTest extends ApiDocumentationTest {
    private final String GOOGLE_OAUTH2_SIGN_IN = "/api/signin/oauth2/google";
    private final String SIGN_UP_TERMS = "/api/signup/terms";

    @Test
    @DisplayName("구글 로그인 시도")
    void requestGoogleOAuth2SignIn() throws Exception {
        ResultActions result = this.mockMvc.perform(
                get(GOOGLE_OAUTH2_SIGN_IN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().is3xxRedirection())
                .andExpect(cookie().exists("oauth2_auth_request"))
                .andDo(document("auth/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters()
                ));
    }

    @Test
    @DisplayName("회원가입 이용약관 동의")
    void agreeSignUpTerms() throws Exception {
        String email = "test@poolaeem.com";

        given(signService.signUpOAuth2User(OauthProvider.GOOGLE, "oauthId", email))
                .willReturn(new User(
                        email,
                        "풀내임",
                        OauthProvider.GOOGLE,
                        "oauthId",
                        null,
                        TermsVersion.V1
                ));

        ResultActions result = this.mockMvc.perform(
                post(SIGN_UP_TERMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SignRequest.SignUpTermsDto(
                                true,
                                OauthProvider.GOOGLE,
                                "oauthId",
                                email
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("auth/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(),
                        requestFields(
                                fieldWithPath("isAgreeTerms").type(JsonFieldType.BOOLEAN).description("true 만 허용"),
                                fieldWithPath("oauthProvider").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.OAUTH_PROVIDER)),
                                fieldWithPath("oauthId").type(JsonFieldType.STRING).description("OAuth2 식별자"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소")
                        ),
                        responseHeaders(
                                headerWithName("access-token").description("access token"),
                                headerWithName("refresh-token").description("refresh token")
                        )
                ));
    }
}