package com.poolaeem.poolaeem.integration.question.problem;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;
import com.poolaeem.poolaeem.question.infra.repository.ProblemOptionRepository;
import com.poolaeem.poolaeem.question.infra.repository.ProblemRepository;
import com.poolaeem.poolaeem.question.presentation.dto.ProblemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문항 수정 테스트")
@Sql(scripts = {
        "classpath:/sql/question/workbook.sql",
        "classpath:/sql/question/problem.sql",
        "classpath:/sql/question/option.sql"
})
class ProblemControllerUpdateTest extends BaseIntegrationTest {
    private final String UPDATE_PROBLEM = "/api/problems/{problemId}";

    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemOptionRepository optionRepository;

    @Test
    @DisplayName("문항을 수정할 수 있다.")
    void testUpdateProblem() throws Exception {
        String problemId = "problem-1";
        ProblemRequest.ProblemUpdate param = new ProblemRequest.ProblemUpdate(
                "New",
                List.of(
                        new ProblemOptionDto("option-2", "세계", false),
                        new ProblemOptionDto("새로운", true),
                        new ProblemOptionDto("뉴스", false)
                )
        );

        Problem beforeProblem = problemRepository.findByIdAndIsDeletedFalse(problemId).get();
        List<ProblemOption> beforeOptions = optionRepository.findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(problemId);
        assertThat(beforeProblem.getQuestion()).isNotEqualTo(param.getQuestion());
        assertThat(beforeOptions.size()).isNotEqualTo(param.getOptions().size());

        ResultActions result = this.mockMvc.perform(
                put(UPDATE_PROBLEM, problemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(param))
                        .accept(MediaType.APPLICATION_JSON)
        );

        Problem afterProblem = problemRepository.findByIdAndIsDeletedFalse(problemId).get();
        List<ProblemOption> afterOptions = optionRepository.findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(problemId);
        assertThat(afterProblem.getQuestion()).isEqualTo(param.getQuestion());
        assertThat(afterOptions).hasSameSizeAs(param.getOptions());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));
    }

    @Test
    @DisplayName("권한이 없는 문제집의 문항은 수정할 수 있다.")
    void testUpdateProblemByOtherUser() throws Exception {
        String problemId = "problem-4";
        ProblemRequest.ProblemUpdate param = new ProblemRequest.ProblemUpdate(
                "New",
                List.of(
                        new ProblemOptionDto( "세계", false),
                        new ProblemOptionDto("새로운", true),
                        new ProblemOptionDto("뉴스", false)
                )
        );

        ResultActions result = this.mockMvc.perform(
                put(UPDATE_PROBLEM, problemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(param))
                        .accept(MediaType.APPLICATION_JSON)
        );
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.ENTITY_NOT_FOUND.getCode())));
    }
}