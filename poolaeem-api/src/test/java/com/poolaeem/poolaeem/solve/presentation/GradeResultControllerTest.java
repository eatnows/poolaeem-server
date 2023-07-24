package com.poolaeem.poolaeem.solve.presentation;

import com.poolaeem.poolaeem.common.exception.common.EntityNotFoundException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.solve.domain.entity.vo.WorkbookResultVo;
import com.poolaeem.poolaeem.test_config.restdocs.ApiDocumentationTest;
import com.poolaeem.poolaeem.test_config.restdocs.DocumentFormatGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentRequest;
import static com.poolaeem.poolaeem.test_config.restdocs.RestDocumentUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("단위: 풀이 결과 컨트롤러 테스트")
class GradeResultControllerTest extends ApiDocumentationTest {
    private final String READ_SOLVED_HISTORY_OF_WORKBOOK = "/api/results/workbooks/{workbookId}/solved/histories";

    @Test
    @DisplayName("문제집의 풀이내역을 조회할 수 있다.")
    void testReadSolvedHistoryOfWorkbook() throws Exception {
        String workbookId = "workbook-id";

        given(gradeResultService.readSolvedHistoryOfWorkbook(any()))
                .willReturn(new SliceImpl<>(List.of(
                        new WorkbookResultVo("result-1", "꼬부기", 10, 9, ZonedDateTime.of(LocalDateTime.of(2023, 7, 24, 15, 13), ZoneId.of("Asia/Seoul"))),
                        new WorkbookResultVo("result-2", "파이리", 10, 7, ZonedDateTime.of(LocalDateTime.of(2023, 7, 24, 15, 15), ZoneId.of("Asia/Seoul"))),
                        new WorkbookResultVo("result-3", "이상해씨", 10, 10, ZonedDateTime.of(LocalDateTime.of(2023, 7, 24, 15, 16), ZoneId.of("Asia/Seoul")))
                ), PageRequest.of(0, 20), true));


        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .param("lastId", "workbook-id2")
                                .param("size", "20")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(document("solve/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("workbookId").description("문제집 id")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer token")
                        ),
                        queryParameters(
                                parameterWithName("lastId").optional().description("마지막 반환된 resultId"),
                                parameterWithName("size").optional().description("반환될 데이터 양")
                        ),
                        responseFields(
                                beneathPath("data"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("조회되지 않은 데이터가 더 남아있는지 여부"),
                                fieldWithPath("results[].resultId").type(JsonFieldType.STRING).description("내역 id"),
                                fieldWithPath("results[].userId").ignored(),
                                fieldWithPath("results[].userName").type(JsonFieldType.STRING).description("유저 이름"),
                                fieldWithPath("results[].totalQuestions").type(JsonFieldType.NUMBER).description("풀이한 총 문항 수"),
                                fieldWithPath("results[].correctCount").type(JsonFieldType.NUMBER).description("맞힌 문항 수"),
                                fieldWithPath("results[].solvedAt").type(JsonFieldType.STRING).attributes(DocumentFormatGenerator.getDateTimeFormat()).description("풀이한 시각")
                        )
                ));
    }

    @Test
    @DisplayName("문제집의 풀이내역이 없을 경우에도 내역을 조회할 수 있다.")
    void testReadSolvedHistoryOfWorkbookForEmptyCase() throws Exception {
        String workbookId = "workbook-id";

        given(gradeResultService.readSolvedHistoryOfWorkbook(any()))
                .willReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 20), false));


        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .param("lastId", "workbook-id2")
                                .param("size", "20")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(document("solve/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }

    @Test
    @DisplayName("권한없는 문제집의 풀이내역은 조회할 수 없다.")
    void testReadSolvedHistoryOfWorkbookForNotCreator() throws Exception {
        String workbookId = "workbook-id";

        given(gradeResultService.readSolvedHistoryOfWorkbook(any()))
                .willThrow(new ForbiddenRequestException("권한이 없는 문제집입니다."));

        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .param("lastId", "workbook-id2")
                                .param("size", "20")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden())
                .andDo(document("solve/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }

    @Test
    @DisplayName("존재하지 않는 문제집의 풀이내역은 조회할 수 없다.")
    void testReadSolvedHistoryOfWorkbookForNotExists() throws Exception {
        String workbookId = "workbook-id";

        given(gradeResultService.readSolvedHistoryOfWorkbook(any()))
                .willThrow(new EntityNotFoundException("문제집이 존재하지 않습니다."));

        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .param("lastId", "workbook-id2")
                                .param("size", "20")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andDo(document("solve/{method-name}",
                        getDocumentRequest(),
                        getDocumentResponse()
                ));
    }
}