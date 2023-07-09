package com.poolaeem.poolaeem.question.presentation;

import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.presentation.dto.ProblemRequest;
import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 문항 관리 컨트롤러 테스트")
class ProblemControllerTest extends ApiDocumentationTest {
    private final String CREATE_PROBLEM = "/api/workbooks/{workbookId}/problem";

    @Test
    @DisplayName("문제집에 문항을 생성할 수 있다.")
    void testCreateProblem() throws Exception {
        String workbookId = "workbook-id";

        ResultActions result = this.mockMvc.perform(
                post(CREATE_PROBLEM, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProblemRequest.ProblemCreate(
                                "Word",
                                List.of(
                                        new ProblemOptionDto("단어", true),
                                        new ProblemOptionDto("세계", false),
                                        new ProblemOptionDto("나무", false)
                                )
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("question/problem/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("workbookId").description("문제집 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        requestFields(
                                fieldWithPath("question").type(JsonFieldType.STRING).description("문제"),
                                fieldWithPath("options[].value").type(JsonFieldType.STRING).description("선택지"),
                                fieldWithPath("options[].isCorrect").type(JsonFieldType.BOOLEAN).description("선택지 정답 여부")
                        )
                ));
    }
}