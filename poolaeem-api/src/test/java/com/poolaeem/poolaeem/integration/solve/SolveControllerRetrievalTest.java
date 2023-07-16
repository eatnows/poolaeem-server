package com.poolaeem.poolaeem.integration.solve;

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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 풀이 관련 통합 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SolveControllerRetrievalTest extends BaseIntegrationTest {
    private final String READ_WORKBOOK_INFO = "/api/workbooks/{workbookId}/solve/introduction";
    @Autowired
    private DataSource dataSource;

    @BeforeAll
    void init() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/file/file.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/workbook.sql"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("문제집 풀이 페이지를 조회할 수 있다.")
    void testReadWorkbookSolveIntroduction() throws Exception {
        String workbookId = "workbook-1";

        this.mockMvc.perform(
                        get(READ_WORKBOOK_INFO, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.name", is("고등영어1")))
                .andExpect(jsonPath("$.data.description", is("고등학교에서 사용하는 영단어 문제입니다.")))
                .andExpect(jsonPath("$.data.author.name", is("풀내임")))
                .andExpect(jsonPath("$.data.author.profileImageUrl", containsString("file-1")))
                .andExpect(jsonPath("$.data.createdAt", is("2023-07-04T23:36+09:00")))
                .andExpect(jsonPath("$.data.problemCount", is(3)))
                .andExpect(jsonPath("$.data.solvedCount", is(2)));
    }

    @Test
    @DisplayName("존재하지 않는 문제집은 조회할 수 없다")
    void testReadWorkbookSolveIntroductionForNotFound() throws Exception {
        String workbookId = "not-exist-workbook";

        this.mockMvc.perform(
                        get(READ_WORKBOOK_INFO, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.WORKBOOK_NOT_FOUND.getCode())));
    }
}