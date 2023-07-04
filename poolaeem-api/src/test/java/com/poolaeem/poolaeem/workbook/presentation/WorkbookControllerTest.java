package com.poolaeem.poolaeem.workbook.presentation;

import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.workbook.presentation.dto.WorkbookRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 문제집 컨트롤러 테스트")
class WorkbookControllerTest extends ApiDocumentationTest {
    private final String CREATE_WORKBOOK = "/api/workbook";

    @Test
    @DisplayName("문제집을 생성할 수 있다")
    void testCreateWorkbook() throws Exception {
        String name = "문제집1";
        String description = "이 문제는 영어단어 모음입니다.";

        ResultActions result = this.mockMvc.perform(
                post(CREATE_WORKBOOK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new WorkbookRequest.WorkbookCreateDto(
                                name,
                                description
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("question/workbook/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("문제집 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("문제집 설명")
                        )
                ));
    }
}