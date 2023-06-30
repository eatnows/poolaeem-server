package com.poolaeem.poolaeem.user.presentation;

import com.poolaeem.poolaeem.config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.poolaeem.poolaeem.config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 프로필 정보 컨트롤러 테스트")
class ProfileInfoControllerTest extends ApiDocumentationTest {

    private final String READ_PROFILE_INFO = "/api/profile/info";

    @Test
    @DisplayName("프로필 정보 조회")
    void readProfileInfo() throws Exception {
        given(profileInfoService.readProfileInfo(anyString()))
                .willReturn(new ProfileDto.ProfileInfo(
                        "user-id",
                        "test@poolaeem.com",
                        "풀내임",
                        "https://image.poolaeem.com/users/profile/1"
                ));

        ResultActions result = this.mockMvc.perform(
                get(READ_PROFILE_INFO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("user/profile/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("userId").type(JsonFieldType.STRING).description("user id"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 주소"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("계정 이름"),
                                fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 조회 주소")
                        )
                ));

    }
}