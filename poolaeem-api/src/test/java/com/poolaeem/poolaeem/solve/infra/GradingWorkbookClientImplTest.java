package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.solve.domain.vo.problem.CheckBoxProblem;
import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;
import com.poolaeem.poolaeem.solve.domain.vo.problem.SubjectiveProblem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 채점 시 문제 데이터 조회 테스트")
class GradingWorkbookClientImplTest {
    @InjectMocks
    private GradingWorkbookClientImpl gradingWorkbookClient;
    @Mock
    private ProblemService problemService;

    @Test
    @DisplayName("문제 타입별로 문제채점 객체를 생성한다.")
    void testNewProblemGradingByProblemType() throws Exception {
        String workbookId = "workbook-id";
        given(problemService.getCorrectAnswers(workbookId))
                .willReturn(List.of(
                        new ProblemVo("problem-1", null, "Mobile", ProblemType.CHECKBOX, 30, List.of(new ProblemOptionVo("option-1", "핸드폰", true), new ProblemOptionVo("option-2", "휴대폰", true))),
                        new ProblemVo("problem-2", null, "Game", ProblemType.SUBJECTIVE, 0, List.of(new ProblemOptionVo("option-3", "게임", true)))
                ));

        Map<String, ProblemGrading> map = gradingWorkbookClient.getCorrectAnswers(workbookId);

        assertThat(map.get("problem-1")).isInstanceOf(CheckBoxProblem.class);
        assertThat(map.get("problem-2")).isInstanceOf(SubjectiveProblem.class);
        assertThat(map.get("problem-1").getType()).isEqualByComparingTo(ProblemType.CHECKBOX);
        assertThat(map.get("problem-2").getType()).isEqualByComparingTo(ProblemType.SUBJECTIVE);
    }
}