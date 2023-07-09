package com.poolaeem.poolaeem.integration.question.problem;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.infra.repository.ProblemOptionRepository;
import com.poolaeem.poolaeem.question.infra.repository.ProblemRepository;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import com.poolaeem.poolaeem.question.presentation.dto.ProblemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("통합: 문항 추가 테스트")
@Sql(scripts = {"classpath:/sql/question/workbook.sql",
        "classpath:/sql/question/problem.sql",
        "classpath:/sql/question/option.sql"})
class ProblemControllerCreationTest extends BaseIntegrationTest {
    private final String CREATE_PROBLEM = "/api/workbooks/{workbookId}/problem";

    @Autowired
    private WorkbookRepository workbookRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemOptionRepository optionRepository;

    @Test
    @DisplayName("문제집에 문항을 추가할 수 있다.")
    void testCreateProblemInWorkbook() throws Exception {
        String workbookId = "workbook-1";

        Workbook workbook = workbookRepository.findByIdAndIsDeletedFalse(workbookId).get();
        long before = problemRepository.findAll().stream()
                .filter(problem -> problem.getWorkbook().getId().equals(workbookId))
                .count();
        assertThat(before).isEqualTo(2);

        ResultActions result = this.mockMvc.perform(
                post(CREATE_PROBLEM, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProblemRequest.ProblemCreate(
                                "Word",
                                List.of(new ProblemOptionDto("단어", true),
                                        new ProblemOptionDto("세계", false))
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        long after = problemRepository.findAll().stream()
                .filter(problem -> problem.getWorkbook().getId().equals(workbookId))
                .count();
        assertThat(after).isEqualTo(before + 1);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));
    }

    @Test
    @DisplayName("문제집에 권한이 없으면 문항을 추가할 수 없다")
    void testCreateProblemByOtherUser() throws Exception {
        String workbookId = "workbook-2";

        ResultActions result = this.mockMvc.perform(
                post(CREATE_PROBLEM, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProblemRequest.ProblemCreate(
                                "Word",
                                List.of(new ProblemOptionDto("단어", true),
                                        new ProblemOptionDto("세계", false))
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.FORBIDDEN.getCode())));
    }

    @Test
    @DisplayName("문항에 선택지를 추가할 수 있다.")
    void testCreateProblemOption() throws Exception {
        String workbookId = "workbook-1";

        long before = optionRepository.findAll().stream()
                .filter(op -> op.getCreatedBy().equals("user-1"))
                .count();
        assertThat(before).isEqualTo(5);

        ResultActions result = this.mockMvc.perform(
                post(CREATE_PROBLEM, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProblemRequest.ProblemCreate(
                                "Word",
                                List.of(new ProblemOptionDto("단어", true),
                                        new ProblemOptionDto("세계", false))
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        long after = optionRepository.findAll().stream()
                .filter(op -> op.getCreatedBy().equals("user-1"))
                .count();
        assertThat(after).isEqualTo(before + 2);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));
    }

    @Test
    @DisplayName("문항은 문제집 마지막 문항의 다음 번호로 추가된다.")
    void testCreateProblemOrder() throws Exception {
        String workbookId = "workbook-1";

        long before = problemRepository.findAll().stream()
                .filter(problem -> problem.getWorkbook().getId().equals(workbookId))
                .filter(problem -> problem.getCreatedBy().equals("user-1"))
                .map(Problem::getOrder)
                .sorted(Comparator.reverseOrder())
                .findFirst().get();
        assertThat(before).isEqualTo(2);

        ResultActions result = this.mockMvc.perform(
                post(CREATE_PROBLEM, workbookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", BEARER_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(new ProblemRequest.ProblemCreate(
                                "Word",
                                List.of(new ProblemOptionDto("단어", true),
                                        new ProblemOptionDto("세계", false))
                        )))
                        .accept(MediaType.APPLICATION_JSON)
        );

        long after = problemRepository.findAll().stream()
                .filter(problem -> problem.getWorkbook().getId().equals(workbookId))
                .filter(problem -> problem.getCreatedBy().equals("user-1"))
                .map(Problem::getOrder)
                .sorted(Comparator.reverseOrder())
                .findFirst().get();
        assertThat(after).isEqualTo(before + 1);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));
    }
}