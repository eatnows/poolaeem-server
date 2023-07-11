package com.poolaeem.poolaeem.integration.question.problem;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.infra.repository.ProblemOptionRepository;
import com.poolaeem.poolaeem.question.infra.repository.ProblemRepository;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문항 삭제 테스트")
@Sql(scripts = {
        "classpath:/sql/question/workbook.sql",
        "classpath:/sql/question/problem.sql",
        "classpath:/sql/question/option.sql"
})
class ProblemControllerDeleteTest extends BaseIntegrationTest {
    private final String DELETE_PROBLEM = "/api/problems/{problemId}";

    @Autowired
    private WorkbookRepository workbookRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemOptionRepository optionRepository;

    @Test
    @DisplayName("문항을 삭제하면 문항과 선택지가 모두 삭제된다.")
    void testDeleteProblem() throws Exception {
        String problemId = "problem-1";

        List<ProblemOption> beforeOptions = optionRepository.findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(problemId);
        Optional<Problem> beforeProblem = problemRepository.findByIdAndIsDeletedFalse(problemId);
        assertThat(beforeOptions).hasSize(2);
        assertThat(beforeProblem).isPresent();

        this.mockMvc.perform(
                        delete(DELETE_PROBLEM, problemId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));

        List<ProblemOption> afterOptions = optionRepository.findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(problemId);
        Optional<Problem> afterProblem = problemRepository.findByIdAndIsDeletedFalse(problemId);
        assertThat(afterOptions).isEmpty();
        assertThat(afterProblem).isNotPresent();
    }

    @Test
    @DisplayName("문항을 삭제하면 문제집 문항수가 감소된다.")
    void testDecreaseProblemCountForDeleteProblem() throws Exception {
        String problemId = "problem-1";

        Problem problem = problemRepository.findByIdAndIsDeletedFalse(problemId).get();
        String workbookId = problem.getWorkbook().getId();
        Workbook before = workbookRepository.findByIdAndIsDeletedFalse(workbookId).get();
        assertThat(before.getProblemCount()).isEqualTo(3);

        this.mockMvc.perform(
                        delete(DELETE_PROBLEM, problemId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));

        Workbook after = workbookRepository.findByIdAndIsDeletedFalse(workbookId).get();
        assertThat(after.getProblemCount()).isEqualTo(before.getProblemCount() - 1);
    }

    @Test
    @DisplayName("권한이 없는 문제집의 문항은 삭제할 수 없다.")
    void testDeleteProblemByOtherUser() throws Exception {
        String problemId = "problem-4";

        this.mockMvc.perform(
                        delete(DELETE_PROBLEM, problemId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.ENTITY_NOT_FOUND.getCode())));
    }
}