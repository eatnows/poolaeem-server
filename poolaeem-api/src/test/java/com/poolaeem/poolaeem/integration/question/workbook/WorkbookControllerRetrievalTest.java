package com.poolaeem.poolaeem.integration.question.workbook;

import com.poolaeem.poolaeem.common.exception.common.EntityNotFoundException;
import com.poolaeem.poolaeem.common.exception.request.ForbiddenRequestException;
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
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문제집 조회 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkbookControllerRetrievalTest extends BaseIntegrationTest {
    private final String READ_WORKBOOK_INFO = "/api/workbooks/{workbookId}";
    private final String READ_WORKBOOK_SOLVE_INTRODUCTION = "/api/workbooks/{workbookId}/solve/introduction";
    private final String READ_MY_WORKBOOKS = "/api/workbooks";


    @Autowired
    private DataSource dataSource;
    @BeforeAll
    public void init() {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("/sql/file/file.sql"));
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
                .andExpect(exception -> assertThat(exception.getResolvedException()).isInstanceOf(EntityNotFoundException.class))
                .andExpect(jsonPath("$.code", is(ApiResponseCode.ENTITY_NOT_FOUND.getCode())));
    }

    @Test
    @DisplayName("문제집 풀이 소개 정보를 조회할 수 있다.")
    void testReadWorkbookSolveIntroduction() throws Exception {
        String workbookId = "workbook-1";

        this.mockMvc.perform(
                        get(READ_WORKBOOK_SOLVE_INTRODUCTION, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.name", is("고등영어1")))
                .andExpect(jsonPath("$.data.description", is("고등학교에서 사용하는 영단어 문제입니다.")))
                .andExpect(jsonPath("$.data.theme", is(WorkbookTheme.PINK.name())))
                .andExpect(jsonPath("$.data.creator.name", is("풀내임")))
                .andExpect(jsonPath("$.data.creator.profileImageUrl", containsString("file-1")))
                .andExpect(jsonPath("$.data.createdAt", is("2023-07-04T23:36+09:00")))
                .andExpect(jsonPath("$.data.problemCount", is(3)))
                .andExpect(jsonPath("$.data.solvedCount", is(2)));
    }

    @Test
    @DisplayName("존재하지 않는 문제집의 풀이 소개는 조회할 수 없다")
    void testReadWorkbookSolveIntroductionForNotFound() throws Exception {
        String workbookId = "not-exist-workbook";

        this.mockMvc.perform(
                        get(READ_WORKBOOK_SOLVE_INTRODUCTION, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.ENTITY_NOT_FOUND.getCode())));
    }

    @Test
    @DisplayName("탈퇴한 유저의 문제집 풀이 소개는 출제가 알 수 없음으로 조회할 수 있다.")
    void testReadWorkbookSolveIntroductionForDeletedCreator() throws Exception{
        String workbookId = "workbook-2";

        mockMvc.perform(
                        get(READ_WORKBOOK_SOLVE_INTRODUCTION, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.creator", nullValue()));
    }

    @Test
    @DisplayName("내 문제집의 목록을 생성일 내림차순으로 조회할 수 있다.")
    void testReadMyWorkbooksOrder() throws Exception {
        ResultActions result = this.mockMvc.perform(
                get(READ_MY_WORKBOOKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        // size 파라미터가 없으면 기본값 10개 조회
                        // lastId가 없으면 전체 조회
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(false)))
                .andExpect(jsonPath("$.data.workbooks.size()", is(2)))
                .andExpect(jsonPath("$.data.workbooks[0].workbookId", is("workbook-3")))
                .andExpect(jsonPath("$.data.workbooks[0].name", is("신생영어 - 상")))
                .andExpect(jsonPath("$.data.workbooks[0].description", is("새롭게 나온 영어 단어 암기 시스템")))
                .andExpect(jsonPath("$.data.workbooks[0].theme", is(WorkbookTheme.PINK.name())))
                .andExpect(jsonPath("$.data.workbooks[0].problemCount", is(0)))
                .andExpect(jsonPath("$.data.workbooks[0].solvedCount", is(0)))
                .andExpect(jsonPath("$.data.workbooks[0].createdAt", is("2023-08-17T00:15+09:00")))
                .andExpect(jsonPath("$.data.workbooks[1].workbookId", is("workbook-1")))
                .andExpect(jsonPath("$.data.workbooks[1].name", is("고등영어1")))
                .andExpect(jsonPath("$.data.workbooks[1].description", is("고등학교에서 사용하는 영단어 문제입니다.")))
                .andExpect(jsonPath("$.data.workbooks[1].theme", is(WorkbookTheme.PINK.name())))
                .andExpect(jsonPath("$.data.workbooks[1].problemCount", is(3)))
                .andExpect(jsonPath("$.data.workbooks[1].solvedCount", is(2)))
                .andExpect(jsonPath("$.data.workbooks[1].createdAt", is("2023-07-04T23:36+09:00")));
    }

    @Test
    @DisplayName("내 문제집의 목록을 size와 lastId로 필터링하여 조회할 수 있다.")
    void testReadMyWorkbooksForSizeAndLastId() throws Exception {
        ResultActions result = this.mockMvc.perform(
                get(READ_MY_WORKBOOKS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .param("size", "1")
                        .param("lastId", "workbook-3")
                        // size 파라미터가 없으면 기본값 10개 조회
                        // lastId가 없으면 전체 조회
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.hasNext", is(false)))
                .andExpect(jsonPath("$.data.workbooks.size()", is(1)))
                .andExpect(jsonPath("$.data.workbooks[0].workbookId", is("workbook-1")))
                .andExpect(jsonPath("$.data.workbooks[0].name", is("고등영어1")))
                .andExpect(jsonPath("$.data.workbooks[0].description", is("고등학교에서 사용하는 영단어 문제입니다.")))
                .andExpect(jsonPath("$.data.workbooks[0].theme", is(WorkbookTheme.PINK.name())))
                .andExpect(jsonPath("$.data.workbooks[0].problemCount", is(3)))
                .andExpect(jsonPath("$.data.workbooks[0].solvedCount", is(2)))
                .andExpect(jsonPath("$.data.workbooks[0].createdAt", is("2023-07-04T23:36+09:00")));
    }
}