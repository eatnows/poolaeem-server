package com.poolaeem.poolaeem.integration.question.workbook;

import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
import com.poolaeem.poolaeem.common.exception.workbook.WorkbookNotFoundException;
import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문제집 조회 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkbookControllerRetrievalTest extends BaseIntegrationTest {
    private final String READ_WORKBOOK_INFO = "/api/workbooks/{workbookId}";

    @Autowired
    private DataSource dataSource;
    @BeforeAll
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/question/workbook.sql"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("문제집의 정보를 조회할 수 있다.")
    void testReadWorkbookInfo() throws Exception {
        String workbookId = "workbook-1";

        ResultActions result = this.mockMvc.perform(
                get(READ_WORKBOOK_INFO, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.name", is("고등영어1")))
                .andExpect(jsonPath("$.data.description", is("고등학교에서 사용하는 영단어 문제입니다.")))
                .andExpect(jsonPath("$.data.problemCount", is(3)))
                .andExpect(jsonPath("$.data.solvedCount", is(2)))
                .andExpect(jsonPath("$.data.theme", is(WorkbookTheme.PINK.name())));
    }

    @Test
    @DisplayName("문제집 관리자가 아니면 정보를 조회할 수 없다.")
    void testReadWorkbookInfoByOtherUser() throws Exception {
        String workbookId = "workbook-2";

        ResultActions result = this.mockMvc.perform(
                get(READ_WORKBOOK_INFO, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isForbidden())
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(ForbiddenRequestException.class))
                .andExpect(jsonPath("$.code", is(ApiResponseCode.FORBIDDEN.getCode())));
    }

    @Test
    @DisplayName("존재하지 않는 문제집을 조회할 수 없다.")
    void testReadWorkbookInfoForNotExistWorkbook() throws Exception {
        String workbookId = "not-exist-workbook";

        ResultActions result = this.mockMvc.perform(
                get(READ_WORKBOOK_INFO, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound())
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(WorkbookNotFoundException.class))
                .andExpect(jsonPath("$.code", is(ApiResponseCode.WORKBOOK_NOT_FOUND.getCode())));
    }
}