package com.poolaeem.poolaeem.user.presentation;

import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.user.domain.dto.ProfileDto;
import com.poolaeem.poolaeem.user.presentation.dto.profile.ProfileInfoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 프로필 정보 컨트롤러 테스트")
class ProfileInfoControllerTest extends ApiDocumentationTest {

    private final String READ_PROFILE_INFO = "/api/profile/info";
    private final String UPDATE_USER_NAME = "/api/profile/name";
    private final String UPDATE_PROFILE_IMAGE = "/api/profile/image";

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

    @Test
    @DisplayName("유저 이름 변경")
    void updateUserName() throws Exception {
        String newUserName = "new-name";

        given(profileInfoService.readProfileInfo(anyString()))
                .willReturn(new ProfileDto.ProfileInfo(
                        "user-id",
                        "test@poolaeem.com",
                        "풀내임",
                        "https://image.poolaeem.com/users/profile/1"
                ));

        ResultActions result = this.mockMvc.perform(
                patch(UPDATE_USER_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProfileInfoRequest.UserNameUpdateDto(newUserName)))
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
                        requestFields(
                                fieldWithPath("userName").type(JsonFieldType.STRING).description("변경할 유저의 이름")
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

    @Test
    @DisplayName("유저의 프로필 이미지를 변경할 수 있다.")
    void updateProfileImage() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "file_name.txt", "text/plain", "<< file content >>".getBytes(StandardCharsets.UTF_8));

        given(profileInfoService.readProfileInfo(anyString()))
                .willReturn(new ProfileDto.ProfileInfo(
                        "user-id",
                        "test@poolaeem.com",
                        "풀내임",
                        "https://image.poolaeem.com/users/profile/1"
                ));

        ResultActions result = this.mockMvc.perform(
                multipart(UPDATE_PROFILE_IMAGE)
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
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
                        requestParts(
                                partWithName("file").optional().description("변경할 이미지 파일 / null 이면 이미지 삭제")
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