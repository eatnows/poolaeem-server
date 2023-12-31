package com.poolaeem.poolaeem.question.presentation;

import com.poolaeem.poolaeem.common.exception.common.EntityNotFoundException;
import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.question.presentation.dto.ProblemRequest;
import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.test_config.restdocs.DocumentLinkGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 문항 관리 컨트롤러 테스트")
class ProblemControllerTest extends ApiDocumentationTest {
    private final String CREATE_PROBLEM = "/api/workbooks/{workbookId}/problem";
    private final String READ_PROBLEM = "/api/problems/{problemId}";
    private final String UPDATE_PROBLEM = "/api/problems/{problemId}";
    private final String DELETE_PROBLEM = "/api/problems/{problemId}";
    private final String READ_PROBLEM_LIST = "/api/workbooks/{workbookId}/problems";
    private final String READ_SOLVE_PROBLEM_LIST = "/api/workbooks/{workbookId}/problems/solve";

    @Test
    @DisplayName("문제집에 문항을 생성할 수 있다.")
    void testCreateProblem() throws Exception {
        String workbookId = "workbook-id";

        ResultActions result = this.mockMvc.perform(
                post(CREATE_PROBLEM, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProblemRequest.ProblemCreate(
                                "Word", ProblemType.CHECKBOX,
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
                                fieldWithPath("type").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PROBLEM_TYPE)),
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
                        ProblemType.CHECKBOX, 30,
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
                                fieldWithPath("type").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PROBLEM_TYPE)),
                                fieldWithPath("timeout").type(JsonFieldType.NUMBER).description("풀이 제한 시간 (초)"),
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

        given(problemService.readProblem(any(), any()))
                .willReturn(new ProblemVo(problemId, null, "Word",
                        ProblemType.CHECKBOX, 30,
                        List.of(
                                new ProblemOptionVo("option-1", "컴퓨터", true),
                                new ProblemOptionVo("option-2", "스마트폰", false)
                        )));

        ResultActions result = this.mockMvc.perform(
                put(UPDATE_PROBLEM, problemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProblemRequest.ProblemUpdate(
                                "Computer",
                                ProblemType.CHECKBOX,
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
                                fieldWithPath("type").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PROBLEM_TYPE)),
                                fieldWithPath("options[].optionId").type(JsonFieldType.STRING).optional().description("선택지 id"),
                                fieldWithPath("options[].value").type(JsonFieldType.STRING).description("선택지 값"),
                                fieldWithPath("options[].isCorrect").type(JsonFieldType.BOOLEAN).description("선택지 정답 여부")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("problemId").type(JsonFieldType.STRING).description("문항 id"),
                                fieldWithPath("question").type(JsonFieldType.STRING).description("문제"),
                                fieldWithPath("type").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PROBLEM_TYPE)),
                                fieldWithPath("timeout").type(JsonFieldType.NUMBER).description("풀이 제한 시간 (초)"),
                                fieldWithPath("options[].optionId").type(JsonFieldType.STRING).description("선택지 id"),
                                fieldWithPath("options[].value").type(JsonFieldType.STRING).description("선택지 값"),
                                fieldWithPath("options[].isCorrect").type(JsonFieldType.BOOLEAN).description("정답 여부")
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

    @Test
    @DisplayName("문항 목록을 조회할 수 있다.")
    void readProblemList() throws Exception {
        String workbookId = "workbook-id";

        given(problemService.readProblemList(anyString(), anyString(), anyInt(), any()))
                .willReturn(new SliceImpl<>(
                        List.of(
                                new ProblemVo("problem-1", "Computer", ProblemType.CHECKBOX, 4, 30),
                                new ProblemVo("problem-2", "Mouse", ProblemType.CHECKBOX, 2, 30),
                                new ProblemVo("problem-2", "Monitor", ProblemType.CHECKBOX, 10, 30),
                                new ProblemVo("problem-2", "keyboard", ProblemType.CHECKBOX, 5, 30)
                        ),
                        PageRequest.of(0, 20),
                        true
                ));

        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM_LIST, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .param("order", "10")
                        .param("size", "20")
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
                        queryParameters(
                                parameterWithName("order").optional().description("문항 순서"),
                                parameterWithName("size").optional().description("한 번에 반환하는 데이터 크기")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("문항이 더 남아있는지 여부"),
                                fieldWithPath("problems[].problemId").type(JsonFieldType.STRING).description("문항 id"),
                                fieldWithPath("problems[].question").type(JsonFieldType.STRING).description("문제"),
                                fieldWithPath("problems[].type").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PROBLEM_TYPE)),
                                fieldWithPath("problems[].optionCount").type(JsonFieldType.NUMBER).description("선택지 개수"),
                                fieldWithPath("problems[].timeout").type(JsonFieldType.NUMBER).description("풀이 제한시간")
                        )
                ));
    }

    @Test
    @DisplayName("풀이 문제들을 조회할 수 있다.")
    void testReadSolveProblems() throws Exception {
        String workbookId = "workbook-id";

        given(problemService.readSolveProblems(anyString(), anyInt(), any()))
                .willReturn(new SliceImpl<>(List.of(
                        new ProblemVo(
                                "problem-1",
                                null,
                                "Computer",
                                ProblemType.CHECKBOX,
                                30,
                                List.of(
                                        new ProblemOptionVo(
                                                "option-1",
                                                null,
                                                "컴퓨터"
                                        ),
                                        new ProblemOptionVo(
                                                "option-2",
                                                null,
                                                "마우스"
                                        ),
                                        new ProblemOptionVo(
                                                "option-3",
                                                null,
                                                "프린터"
                                        )
                                )
                        ),
                        new ProblemVo(
                                "problem-2",
                                null,
                                "Speaker",
                                ProblemType.CHECKBOX,
                                30,
                                List.of(
                                        new ProblemOptionVo(
                                                "option-1",
                                                null,
                                                "마우스"
                                        ),
                                        new ProblemOptionVo(
                                                "option-2",
                                                null,
                                                "발표자"
                                        )
                                )
                        ),
                        new ProblemVo(
                                "problem-3",
                                null,
                                "Food",
                                ProblemType.CHECKBOX,
                                30,
                                List.of(
                                        new ProblemOptionVo(
                                                "option-1",
                                                null,
                                                "음식"
                                        ),
                                        new ProblemOptionVo(
                                                "option-2",
                                                null,
                                                "키보드"
                                        )
                                )
                        )
                )));

        this.mockMvc.perform(
                        get(READ_SOLVE_PROBLEM_LIST, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("order", "20")
                                .param("size", "20")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(document("question/problem/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("workbookId").description("문제집 id")
                        ),
                        queryParameters(
                                parameterWithName("order").optional().description("문제집의 반환받은 문제 개수 (반환 받은 문제의 마지막 순서)"),
                                parameterWithName("size").optional().description("반환 받을 데이터 크기 (기본값 20, 최대값 20)")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 문제 존재 여부"),
                                fieldWithPath("problems[].problemId").type(JsonFieldType.STRING).description("문항 id"),
                                fieldWithPath("problems[].question").type(JsonFieldType.STRING).description("문제"),
                                fieldWithPath("problems[].type").type(JsonFieldType.STRING).description(DocumentLinkGenerator.generateLinkCode(DocumentLinkGenerator.DocUrl.PROBLEM_TYPE)),
                                fieldWithPath("problems[].timeout").type(JsonFieldType.NUMBER).description("문항별 제한시간"),
                                fieldWithPath("problems[].optionCount").ignored().type(JsonFieldType.NUMBER).description("선택지 개수"),
                                fieldWithPath("problems[].options[].optionId").type(JsonFieldType.STRING).description("선택지 id"),
                                fieldWithPath("problems[].options[].value").type(JsonFieldType.STRING).description("선택지 값")
                        )
                ));
    }

    @Test
    @DisplayName("문제집이 존재하지 않으면 풀이할 문항들을 조회할 수 없다")
    void testReadSolveProblemsForNotExistWorkbook() throws Exception {
        String workbookId = "workbook-id";

        given(problemService.readSolveProblems(anyString(), anyInt(), any()))
                .willThrow(new EntityNotFoundException());

        this.mockMvc.perform(
                        get(READ_SOLVE_PROBLEM_LIST, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andDo(document("question/problem/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}