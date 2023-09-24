package com.poolaeem.poolaeem.integration.solve;

import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.service.ResultRecord;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SelectAnswer;
import com.poolaeem.poolaeem.solve.infra.repository.AnswerResultRepository;
import com.poolaeem.poolaeem.solve.infra.repository.ProblemResultRepository;
import com.poolaeem.poolaeem.solve.presentation.dto.GradingRequest;
import org.apache.catalina.util.RequestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 채점 결과 수집 테스트")
@Sql(scripts = {
        "classpath:/sql/common/schema.sql",
        "classpath:/sql/question/workbook.sql",
        "classpath:/sql/question/problem.sql",
        "classpath:/sql/question/option.sql",
        "classpath:/sql/grade/workbook_result.sql",
        "classpath:/sql/grade/problem_result.sql",
        "classpath:/sql/grade/answer_result.sql"
})
class GradingControllerResultTest extends BaseIntegrationTest {
    private final String GRADE_PROBLEMS = "/api/workbooks/{workbookId}/grade";

    @Autowired
    private ProblemResultRepository problemResultRepository;
    @Autowired
    private AnswerResultRepository answerResultRepository;

    @Test
    @DisplayName("로그인을 하지 않은 유저의 답안은 수집하지 않는다.")
    void testSaveGradeResultForNotLoggedInUser() throws Exception {
        ResultRecord resultRecord = Mockito.mock(ResultRecord.class);

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

        verify(resultRecord, times(0)).asyncSaveProblemResult(anyList());
        verify(resultRecord, times(0)).asyncSaveAnswerResult(anyList());
    }

    @Test
    @DisplayName("로그인한 유저의 채점 결과를 수집하여 저장한다.")
    void testSaveGradeResultForLoggedInUser() throws Exception {
        String workbookId = "workbook-1";

        long beforeProblemResultCount = problemResultRepository.findAll().stream()
                .filter(result -> result.getUserId().equals("user-1"))
                .count();
        long beforeAnswerResultCount = answerResultRepository.findAll().stream()
                .filter(result -> result.getUserId().equals("user-1"))
                .count();

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
                ).andExpect(status().isOk());

        // 비동기 수집 반영을 위하여 시간 텀을 줌
        Thread.sleep(100);
        long afterProblemResultCount = problemResultRepository.findAll().stream()
                .filter(result -> result.getUserId().equals("user-1"))
                .count();
        long afterAnswerResultCount = answerResultRepository.findAll().stream()
                .filter(result -> result.getUserId().equals("user-1"))
                .count();

        assertThat(afterProblemResultCount).isGreaterThan(beforeProblemResultCount);
        assertThat(afterAnswerResultCount).isGreaterThan(beforeAnswerResultCount);
    }
}