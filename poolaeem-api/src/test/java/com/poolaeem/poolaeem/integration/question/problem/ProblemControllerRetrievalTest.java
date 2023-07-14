package com.poolaeem.poolaeem.integration.question.problem;

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
import org.springframework.test.web.servlet.ResultActions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문항 조회 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProblemControllerRetrievalTest extends BaseIntegrationTest {
    private final String READ_PROBLEM = "/api/problems/{problemId}";
    private final String READ_PROBLEM_LIST = "/api/workbooks/{workbookId}/problems";

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    void beforeAll() {
        try (Connection conn = dataSource.getConnection()){
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/workbook.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/problem.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/option.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("문항의 정보를 조회할 수 있다.")
    void testReadProblem() throws Exception {
        String problemId = "problem-2";

        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM, problemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.problemId", is(problemId)))
                .andExpect(jsonPath("$.data.question", is("School")))
                .andExpect(jsonPath("$.data.type", is("CHECKBOX")))
                .andExpect(jsonPath("$.data.timeout", is(30)))
                .andExpect(jsonPath("$.data.options[0].optionId", is("option-3")))
                .andExpect(jsonPath("$.data.options[0].value", is("스쿨버스")))
                .andExpect(jsonPath("$.data.options[0].isCorrect", is(false)))
                .andExpect(jsonPath("$.data.options[1].optionId", is("option-4")))
                .andExpect(jsonPath("$.data.options[1].value", is("수업")))
                .andExpect(jsonPath("$.data.options[1].isCorrect", is(false)))
                .andExpect(jsonPath("$.data.options[2].optionId", is("option-5")))
                .andExpect(jsonPath("$.data.options[2].value", is("학교")))
                .andExpect(jsonPath("$.data.options[2].isCorrect", is(true)));
    }

    @Test
    @DisplayName("권한이 없는 문제집의 문항 정보는 조회할 수 없다.")
    void testReadProblemByOtherUser() throws Exception {
        String problemId = "problem-4";

        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM, problemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.ENTITY_NOT_FOUND.getCode())));
    }

    @Test
    @DisplayName("문항 목록을 조회할 수 있다.")
    void testReadProblemList() throws Exception {
        String workbookId = "workbook-1";

        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM_LIST, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .param("order", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(false)))
                .andExpect(jsonPath("$.data.problems.size()", is(3)))
                .andExpect(jsonPath("$.data.problems[0].problemId", is("problem-1")))
                .andExpect(jsonPath("$.data.problems[0].question", is("Word")))
                .andExpect(jsonPath("$.data.problems[1].problemId", is("problem-2")))
                .andExpect(jsonPath("$.data.problems[1].question", is("School")))
                .andExpect(jsonPath("$.data.problems[2].problemId", is("problem-3")))
                .andExpect(jsonPath("$.data.problems[2].question", is("Video")));
    }

    @Test
    @DisplayName("문항 목록이 더 존재하면 hasNext를 true로 반환한다")
    void testHasNextTrueInReadProblemList() throws Exception {
        String workbookId = "workbook-1";

        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM_LIST, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .param("order", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(true)))
                .andExpect(jsonPath("$.data.problems.size()", is(2)))
                .andExpect(jsonPath("$.data.problems[0].problemId", is("problem-1")))
                .andExpect(jsonPath("$.data.problems[0].question", is("Word")))
                .andExpect(jsonPath("$.data.problems[1].problemId", is("problem-2")))
                .andExpect(jsonPath("$.data.problems[1].question", is("School")));
    }

    @Test
    @DisplayName("order 다음 목록을 조회할 수 있다.")
    void testPaginationInProblemList() throws Exception {
        String workbookId = "workbook-1";

        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM_LIST, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .param("order", "2")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(false)))
                .andExpect(jsonPath("$.data.problems.size()", is(1)))
                .andExpect(jsonPath("$.data.problems[0].problemId", is("problem-3")))
                .andExpect(jsonPath("$.data.problems[0].question", is("Video")));
    }

    @Test
    @DisplayName("권한이 없는 문제집의 문항 목록은 조회할 수 없다.")
    void testReadProblemListByOtherUser() throws Exception {
        String workbookId = "workbook-2";

        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM_LIST, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .param("order", "500")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.FORBIDDEN.getCode())));
    }

    @Test
    @DisplayName("문항 목록 정렬 순서는 오래된순으로 정렬된다")
    void testReadProblemListOrder() throws Exception {
        String workbookId = "workbook-1";

        ResultActions result = this.mockMvc.perform(
                get(READ_PROBLEM_LIST, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .param("order", "0")
                        .param("size", "3")
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(false)))
                .andExpect(jsonPath("$.data.problems[0].problemId", is("problem-1")))
                .andExpect(jsonPath("$.data.problems[0].question", is("Word")))
                .andExpect(jsonPath("$.data.problems[1].problemId", is("problem-2")))
                .andExpect(jsonPath("$.data.problems[1].question", is("School")))
                .andExpect(jsonPath("$.data.problems[2].problemId", is("problem-3")))
                .andExpect(jsonPath("$.data.problems[2].question", is("Video")));
    }
}