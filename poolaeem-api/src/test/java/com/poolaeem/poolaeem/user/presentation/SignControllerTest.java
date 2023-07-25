package com.poolaeem.poolaeem.user.presentation;

import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: OAuth2 로그인 테스트")
class SignControllerTest extends ApiDocumentationTest {
    private final String GOOGLE_OAUTH2_SIGN_IN = "/api/signin/oauth2/google";
    private final String SIGN_UP_TERMS = "/api/signup/terms";
    private final String GENERATE_ACCESS_TOKEN = "/api/access-token/refresh";
    private final String DELETE_USER = "/api/users/{userId}";
    private final String BEARER_REFRESH_TOKEN = "Bearer eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwb29sYWVlbSIsInN1YiI6IlJlZnJlc2giLCJjb2RlIjoidXNlci1pZCIsImVtYWlsIjoidGVzdEBwb29sYWVlbS5jb20iLCJuYW1lIjoi7ZKA64K07J6EIiwiaWF0IjoxNjg3NjEzOTIzLCJleHAiOjM2ODg5MDk5MjN9.kt98uhYRuZsA7-N2g44qhp3fXTVRnvAivauB_DoJRVu91_G5GQZjzfocYbfpS7Jd05QpcNitRxQuKZgo0B9yqwo2thKpHevkatghwhESzsYqA2hfTXaR9jdDXuTXAMlFciLyErxrnNTfMtPhaeFq_dZg9YPCaT-36rsqEXg-yf2cGGl9iz0oCXB3pHZgqmADip5huRiHISvTOdt-Z2IOAfJ5B-cUaz89JNneSGoQl1G-es9NP2b_GWg1k5FZjMBXxE_ZHpOL8lo4le85CbcZCMbOoGHmKoNqh81eXRv3itgqqRAWBtbDf9oqpUUIS5Ygk1y5RZF4cNpapmfAS89MVA";

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

    @Test
    @DisplayName("리프레시 토큰으로 액세스 토큰 발급")
    void testGenerateAccessTokenByRefreshToken() throws Exception {
        given(signService.generateAccessTokenByRefreshToken(anyString()))
                .willReturn("access-token");

        ResultActions result = this.mockMvc.perform(
                post(GENERATE_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Refresh", BEARER_REFRESH_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("auth/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(),
                        requestHeaders(
                                headerWithName("Refresh").description("리프레시 토큰")
                        ),
                        responseHeaders(
                                headerWithName("access-token").description("액세스 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("유저가 탈퇴할 수 있다.")
    void testDeleteUser() throws Exception {
        String userId = "user-id";

        mockMvc.perform(
                        delete(DELETE_USER, userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(document("auth/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("userId").description("유저 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        )
                ));
    }

    @Test
    @DisplayName("유저id가 토큰과 다르면 탈퇴할 수 없다")
    void testDeleteUserForOtherUserId() throws Exception {
        String userId = "other-user-id";

        doThrow(new ForbiddenRequestException()).when(signService).deleteUser(anyString(), anyString());

        mockMvc.perform(
                        delete(DELETE_USER, userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden())
                .andDo(document("auth/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}