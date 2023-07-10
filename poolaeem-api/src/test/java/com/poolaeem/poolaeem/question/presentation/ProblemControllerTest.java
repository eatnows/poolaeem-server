package com.poolaeem.poolaeem.question.presentation;

import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 문항 관리 컨트롤러 테스트")
class ProblemControllerTest extends ApiDocumentationTest {
    private final String CREATE_PROBLEM = "/api/workbooks/{workbookId}/problem";
    private final String READ_PROBLEM = "/api/problems/{problemId}";
    private final String UPDATE_PROBLEM = "/api/problems/{problemId}";
    private final String DELETE_PROBLEM = "/api/problems/{problemId}";

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

    @Test
    @DisplayName("문항을 조회를 할 수 있다.")
    void testReadProblem() throws Exception {
        String problemId = "problem-id";

        given(problemService.readProblem(any(), any()))
                .willReturn(new ProblemVo(problemId, null, "Word",
                        List.of(
                                new ProblemOptionVo("option-1", "단어", true),
                                new ProblemOptionVo("option-2", "세계", false)
                        )));


        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM, problemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("question/problem/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("problemId").description("문항 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("problemId").type(JsonFieldType.STRING).description("문항 id"),
                                fieldWithPath("question").type(JsonFieldType.STRING).description("문제"),
                                fieldWithPath("options[].optionId").type(JsonFieldType.STRING).description("선택지 id"),
                                fieldWithPath("options[].value").type(JsonFieldType.STRING).description("선택지 값"),
                                fieldWithPath("options[].isCorrect").type(JsonFieldType.BOOLEAN).description("정답 여부")
                        )
                ));
    }

    @Test
    @DisplayName("문항을 수정할 수 있다.")
    void testUpdateProblem() throws Exception {
        String problemId = "problem-id";

        ResultActions result = this.mockMvc.perform(
                put(UPDATE_PROBLEM, problemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProblemRequest.ProblemUpdate(
                                "Computer",
                                List.of(
                                        new ProblemOptionDto("option-1", "컴퓨터", true),
                                        new ProblemOptionDto("스마트폰", false)
                                )
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("question/problem/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("problemId").description("문제 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        requestFields(
                                fieldWithPath("question").type(JsonFieldType.STRING).description("문제"),
                                fieldWithPath("options[].optionId").type(JsonFieldType.STRING).optional().description("선택지 id"),
                                fieldWithPath("options[].value").type(JsonFieldType.STRING).description("선택지 값"),
                                fieldWithPath("options[].isCorrect").type(JsonFieldType.BOOLEAN).description("선택지 정답 여부")
                        )
                ));
    }

    @Test
    @DisplayName("문항을 삭제할 수 있다.")
    void testDeleteProblem() throws Exception {
        String problemId = "problem-id";

        ResultActions result = this.mockMvc.perform(
                delete(DELETE_PROBLEM, problemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("question/problem/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("problemId").description("삭제할 문항 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        )
                ));
    }
}