package com.poolaeem.poolaeem.integration.question.workbook;

import com.poolaeem.poolaeem.common.response.ApiResponseCode;
import com.poolaeem.poolaeem.integration.base.BaseIntegrationTest;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("통합: 문제집 삭제 테스트")
@Sql(scripts = {
        "classpath:/sql/question/workbook.sql",
        "classpath:/sql/question/problem.sql",
        "classpath:/sql/question/option.sql"
})
class WorkbookControllerDeleteTest extends BaseIntegrationTest {
    private final String DELETE_WORKBOOK = "/api/workbooks/{workbookId}";

    @Autowired
    private WorkbookRepository workbookRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemOptionRepository problemOptionRepository;

    @Test
    @DisplayName("문제집을 삭제하면 문항과 선택지도 모두 삭제된다.")
    void testDeleteWorkbook() throws Exception {
        String workbookId = "workbook-1";

        Optional<Workbook> beforeWorkbook = workbookRepository.findByIdAndIsDeletedFalse(workbookId);
        List<ProblemVo> beforeProblems = problemRepository.findAllProblemIdAndTypeByWorkbook(new Workbook(workbookId, null, null, null, 0, 0, null));
        List<ProblemOptionVo> beforeOptions = problemOptionRepository.findAllDtoByProblemIdInAndIsDeletedFalse(beforeProblems.stream().map(ProblemVo::getProblemId).toList());

        assertThat(beforeWorkbook).isPresent();
        assertThat(beforeProblems).isNotEmpty();
        assertThat(beforeOptions).isNotEmpty();

        mockMvc.perform(
                        delete(DELETE_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(0)));

        Optional<Workbook> afterWorkbook = workbookRepository.findByIdAndIsDeletedFalse(workbookId);
        List<ProblemVo> afterProblems = problemRepository.findAllProblemIdAndTypeByWorkbook(new Workbook(workbookId, null, null, null, 0, 0, null));
        List<ProblemOptionVo> afterOptions = problemOptionRepository.findAllDtoByProblemIdInAndIsDeletedFalse(beforeProblems.stream().map(ProblemVo::getProblemId).toList());

        assertThat(afterWorkbook).isEmpty();
        assertThat(afterProblems).isEmpty();
        assertThat(afterOptions).isEmpty();
    }

    @Test
    @DisplayName("다른 유저의 문제집은 삭제할 수 없다.")
    void testDeleteWorkbookForNotCreator() throws Exception {
        String workbookId = "workbook-2";

        mockMvc.perform(
                        delete(DELETE_WORKBOOK, workbookId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", BEARER_ACCESS_TOKEN)
                                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code", is(ApiResponseCode.FORBIDDEN.getCode())));
    }
}