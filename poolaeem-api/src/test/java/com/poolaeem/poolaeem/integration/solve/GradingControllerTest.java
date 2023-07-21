package com.poolaeem.poolaeem.integration.solve;

import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.solve.domain.dto.SelectAnswer;
import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.presentation.dto.GradingRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문제 채점 테스트")
@Sql(scripts = {
        "classpath:/sql/question/workbook.sql",
        "classpath:/sql/question/problem.sql",
        "classpath:/sql/question/option.sql"
})
class GradingControllerTest extends BaseIntegrationTest {
    private final String GRADE_PROBLEMS = "/api/workbooks/{workbookId}/grade";

    @Test
    @DisplayName("문제집 풀이를 채점 후 결과를 받을 수 있다.")
    void testGradeWorkbook() throws Exception {
        String workbookId = "workbook-1";

        this.mockMvc.perform(
                        post(GRADE_PROBLEMS, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .content(objectMapper.writeValueAsString(new GradingRequest.WorkbookGrade(
                                        "고길동",
                                        List.of(
                                                new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1", "단어"), new SelectAnswer("option-2", "세계")))),
                                                new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-5", "학교")))),
                                                new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-6", "비디오"), new SelectAnswer("option-7", "동영상"))))
                                        )
                                )))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.name", is("고길동")))
                .andExpect(jsonPath("$.data.totalProblems", is(3)))
                .andExpect(jsonPath("$.data.correctCount", is(2)))
                .andExpect(jsonPath("$.data.accuracyRate", is(67)));
    }

    @Test
    @DisplayName("로그인을 하지 않은 유저도 문제를 풀고 채점할 수 있다.")
    void testGradeWorkbookForNotLoggedInUser() throws Exception {
        String workbookId = "workbook-1";

        this.mockMvc.perform(
                        post(GRADE_PROBLEMS, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new GradingRequest.WorkbookGrade(
                                        "고길동",
                                        List.of(
                                                new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1", "단어"), new SelectAnswer("option-2", "세계")))),
                                                new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-5", "학교")))),
                                                new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-6", "비디오"))))
                                        )
                                )))
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)))
                .andExpect(jsonPath("$.data.name", is("고길동")))
                .andExpect(jsonPath("$.data.totalProblems", is(3)))
                .andExpect(jsonPath("$.data.correctCount", is(1)))
                .andExpect(jsonPath("$.data.accuracyRate", is(33)));
    }
}