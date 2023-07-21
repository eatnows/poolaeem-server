package com.poolaeem.poolaeem.solve.presentation;


import com.poolaeem.poolaeem.solve.domain.dto.SelectAnswer;
import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.presentation.dto.GradingRequest;
import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 문제 풀이 컨트롤러 테스트")
class GradingControllerTest extends ApiDocumentationTest {
    private final String GRADE_WORKBOOK = "/api/workbooks/{workbookId}/grade";

    @Test
    @DisplayName("푼 문제를 채점할 수 있다.")
    void testSolveWorkbook() throws Exception {
        String workbookId = "workbook-id";

        given(gradingService.gradeWorkbook(any()))
                .willReturn(List.of(true, false, true, true, false, true, true, false, true, false));

        ResultActions result = this.mockMvc.perform(
                post(GRADE_WORKBOOK, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new GradingRequest.WorkbookGrade(
                                "고길동",
                                List.of(new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1", "컴퓨터")))),
                                        new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-2", "Application")))),
                                        new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-3", "English")))),
                                        new UserAnswer("problem-4", new Answer(List.of(new SelectAnswer("option-4", "Windows")))),
                                        new UserAnswer("problem-5", new Answer(List.of(new SelectAnswer("option-5", "Beautiful"), new SelectAnswer("option-6", "Pretty")))),
                                        new UserAnswer("problem-6", new Answer(List.of(new SelectAnswer("option-7", "Bed")))),
                                        new UserAnswer("problem-7", new Answer(List.of(new SelectAnswer("option-8", "배고프다")))),
                                        new UserAnswer("problem-8", new Answer(List.of(new SelectAnswer("option-9", "5")))),
                                        new UserAnswer("problem-9", new Answer(List.of(new SelectAnswer("option-10", "Phone")))),
                                        new UserAnswer("problem-10", new Answer(List.of(new SelectAnswer("option-11", "성공하다"), new SelectAnswer("option-12", "성공"))))
                                )
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("solve/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("workbookId").description("문제집 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").optional().description("Bearer token")
                        ),
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("풀이자 별명"),
                                fieldWithPath("problems[].problemId").type(JsonFieldType.STRING).description("문항 id"),
                                fieldWithPath("problems[].answer.values[].optionId").type(JsonFieldType.STRING).description("선택지 id"),
                                fieldWithPath("problems[].answer.values[].answer").type(JsonFieldType.STRING).description("객관식 답"),
                                fieldWithPath("problems[].answer.value").ignored().type(JsonFieldType.STRING).description("주관식 답")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("풀이자 별명"),
                                fieldWithPath("totalProblems").type(JsonFieldType.NUMBER).description("푼 문항 수"),
                                fieldWithPath("correctCount").type(JsonFieldType.NUMBER).description("맞힌 수"),
                                fieldWithPath("accuracyRate").type(JsonFieldType.NUMBER).description("정답률")
                        )
                ));
    }

    @Test
    @DisplayName("풀이자 이름이 존재하지 않으면 채점할 수 없다.")
    void testGradeWorkbookForNotExistName() throws Exception {
        String workbookId = "workbook-id";

        ResultActions result = this.mockMvc.perform(
                post(GRADE_WORKBOOK, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new GradingRequest.WorkbookGrade(
                                null,
                                List.of(new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1", "컴퓨터")))))
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest())
                .andDo(document("solve/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }

    @Test
    @DisplayName("풀이가 존재하지 않으면 채점할 수 없다.")
    void testGradeWorkbookForNotExistAnswer() throws Exception {
        String workbookId = "workbook-id";

        ResultActions result = this.mockMvc.perform(
                post(GRADE_WORKBOOK, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new GradingRequest.WorkbookGrade(
                                "고길동",
                                new ArrayList<>()
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest())
                .andDo(document("solve/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}