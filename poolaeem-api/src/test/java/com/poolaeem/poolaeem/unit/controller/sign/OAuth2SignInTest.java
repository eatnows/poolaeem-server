package com.poolaeem.poolaeem.unit.controller.sign;

import com.poolaeem.poolaeem.config.restdocs.ApiDocumentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.poolaeem.poolaeem.config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: OAuth2 로그인 테스트")
class OAuth2SignInTest extends ApiDocumentationTest {

    private final String GOOGLE_OAUTH2_SIGN_IN = "/api/signin/oauth2/google";
    private final String GOOGLE_LOGIN_AUTHENTICATION = "/login/oauth2/code/google";

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
                .andDo(document("sign/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters()
                ));
    }
}
