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
}