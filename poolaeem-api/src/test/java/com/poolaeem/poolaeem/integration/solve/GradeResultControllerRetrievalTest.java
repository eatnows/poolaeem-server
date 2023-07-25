package com.poolaeem.poolaeem.integration.solve;

import com.poolaeem.poolaeem.common.exception.auth.UnAuthorizationException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 풀이 내역 조회 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GradeResultControllerRetrievalTest extends BaseIntegrationTest {
    private final String READ_SOLVED_HISTORY_OF_WORKBOOK = "/api/results/workbooks/{workbookId}/solved/histories";

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    void init() {
        try (Connection conn = dataSource.getConnection()){
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/workbook.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/problem.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/option.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/grade/workbook_result.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/grade/problem_result.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/grade/answer_result.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("문제집의 풀이내역을 생성 내림차순으로 조회할 수 있다. (lastId와 size가 없어도)")
    void testReadSolvedHistoryOfWorkbook() throws Exception {
        String workbookId = "workbook-1";

        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(false)))
                .andExpect(jsonPath("$.data.results", hasSize(2)))
                .andExpect(jsonPath("$.data.results[0].resultId", is("w-result-2")))
                .andExpect(jsonPath("$.data.results[0].userId", nullValue()))
                .andExpect(jsonPath("$.data.results[0].userName", is("원어민")))
                .andExpect(jsonPath("$.data.results[0].totalQuestions", is(3)))
                .andExpect(jsonPath("$.data.results[0].correctCount", is(2)))
                .andExpect(jsonPath("$.data.results[0].solvedAt", is("2023-07-24T06:40+09:00")))
                .andExpect(jsonPath("$.data.results[1].resultId", is("w-result-1")))
                .andExpect(jsonPath("$.data.results[1].userId", nullValue()))
                .andExpect(jsonPath("$.data.results[1].userName", is("풀내임")))
                .andExpect(jsonPath("$.data.results[1].totalQuestions", is(3)))
                .andExpect(jsonPath("$.data.results[1].correctCount", is(3)))
                .andExpect(jsonPath("$.data.results[1].solvedAt", is("2023-07-24T06:38+09:00")));
    }

    @Test
    @DisplayName("문제집의 풀이내역이 더 존재하는지를 hasNext로 알 수 있다.")
    void testReadSolvedHistoryOfWorkbookForHasNext() throws Exception {
        String workbookId = "workbook-1";

        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .param("size", "1")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(true)))
                .andExpect(jsonPath("$.data.results", hasSize(1)))
                .andExpect(jsonPath("$.data.results[0].resultId", is("w-result-2")))
                .andExpect(jsonPath("$.data.results[0].userId", nullValue()))
                .andExpect(jsonPath("$.data.results[0].userName", is("원어민")))
                .andExpect(jsonPath("$.data.results[0].totalQuestions", is(3)))
                .andExpect(jsonPath("$.data.results[0].correctCount", is(2)))
                .andExpect(jsonPath("$.data.results[0].solvedAt", is("2023-07-24T06:40+09:00")));
    }

    @Test
    @DisplayName("다음 문제집의 풀이내역을 조회할 수 있다.")
    void testReadNextSolvedHistoryOfWorkbook() throws Exception {
        String workbookId = "workbook-1";

        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .param("lastId", "w-result-2")
                                .param("size", "10")
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(false)))
                .andExpect(jsonPath("$.data.results", hasSize(1)))
                .andExpect(jsonPath("$.data.results[0].userId", nullValue()))
                .andExpect(jsonPath("$.data.results[0].userName", is("풀내임")))
                .andExpect(jsonPath("$.data.results[0].totalQuestions", is(3)))
                .andExpect(jsonPath("$.data.results[0].correctCount", is(3)))
                .andExpect(jsonPath("$.data.results[0].solvedAt", is("2023-07-24T06:38+09:00")));
    }

    @Test
    @DisplayName("권한이 없는 문제집의 풀이내역은 조회할 수 없다.")
    void testReadSolvedHistoryOfWorkbookByNotCreator() throws Exception {
        String workbookId = "workbook-2";

        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.FORBIDDEN.getCode())))
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(ForbiddenRequestException.class));
    }

    @Test
    @DisplayName("로그인하지 않으면 문제집의 풀이내역을 조회할 수 없다.")
    void testReadSolvedHistoryOfWorkbookByNotLoggedIn() throws Exception {
        String workbookId = "workbook-2";

        this.mockMvc.perform(
                        get(READ_SOLVED_HISTORY_OF_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.UNAUTHORIZED.getCode())))
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(UnAuthorizationException.class));
    }
}