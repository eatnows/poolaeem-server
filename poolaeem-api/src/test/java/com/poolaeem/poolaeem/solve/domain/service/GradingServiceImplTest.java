package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.common.event.WorkbookEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SelectAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.MultipleOptionAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.problem.CheckBoxProblem;
import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;
import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;
import com.poolaeem.poolaeem.solve.infra.GradingWorkbookClient;
import com.poolaeem.poolaeem.solve.infra.repository.WorkbookResultRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 풀이 관련 테스트")
class GradingServiceImplTest {
    @InjectMocks
    private GradingServiceImpl solveService;
    @Mock
    private GradingWorkbookClient gradingWorkbookClient;
    @Mock
    private WorkbookResultRepository workbookResultRepository;
    @Mock
    private WorkbookEventsPublisher workbookEventsPublisher;

    @Test
    @DisplayName("풀이한 문제를 채점할 수 있다.")
    void testGradeProblems() throws Exception {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                "고길동",
                List.of(
                        new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1","개발")))),
                        new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-2","Apple"), new SelectAnswer("option-3", "Microsoft")))),
                        new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-4","Study")))),
                        new UserAnswer("problem-4", new Answer(List.of(new SelectAnswer("option-5","A"), new SelectAnswer("option-6", "B"))))
                )
        );

        given(gradingWorkbookClient.getCorrectAnswers(workbookId))
                .willReturn(getCorrectAnswerMap());

        List<Boolean> results = solveService.gradeWorkbook(param);
        assertThat(results).hasSize(4);
        assertThat(results).containsExactly(true, true, true, false);
    }

    @Test
    @DisplayName("복수 정답일 경우 개수가 모두 맞아야한다.")
    void testGradeProblemsForAnswerSize() throws Exception {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                "고길동",
                List.of(
                        new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1","개발")))),
                        new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-2","Apple"), new SelectAnswer("option-3", "Microsoft")))),
                        new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-4","Study")))),
                        new UserAnswer("problem-4", new Answer(List.of(new SelectAnswer("option-6", "B"))))
                )
        );

        given(gradingWorkbookClient.getCorrectAnswers(workbookId))
                .willReturn(getCorrectAnswerMap());

        List<Boolean> results = solveService.gradeWorkbook(param);
        assertThat(results).hasSize(4);
        assertThat(results).containsExactly(true, true, true, false);
    }

    @Test
    @DisplayName("복수 정답일 경우 모든 정답을 입력 했을 경우에만 맞혔다고 판단한다.")
    void testGradeProblemsForMultipleCorrectAnswer() throws Exception {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                "고길동",
                List.of(
                        new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1","개발")))),
                        new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-2","Apple"), new SelectAnswer("option-3", "Microsoft")))),
                        new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-4","Study")))),
                        new UserAnswer("problem-4", new Answer(List.of(new SelectAnswer("option-6","B"), new SelectAnswer("option-7", "C"))))
                )
        );

        given(gradingWorkbookClient.getCorrectAnswers(workbookId))
                .willReturn(getCorrectAnswerMap());

        List<Boolean> results = solveService.gradeWorkbook(param);
        assertThat(results).hasSize(4);
        assertThat(results).containsExactly(true, true, true, true);
    }

    @Test
    @DisplayName("같은 정답이 중복으로 존재할 경우 전부 맞추어야 맞힌것으로 판단한다.")
    void testGradeProblemsForDuplicatedCorrectAnswer1() throws Exception {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                "고길동",
                List.of(
                        new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1", "B"), new SelectAnswer("option-2", "B"))))
                )
        );

        Map<String, ProblemGrading> map = new HashMap<>();
        map.put("problem-1", new CheckBoxProblem("problem-1", ProblemType.CHECKBOX, new MultipleOptionAnswer(List.of(new SelectAnswer("option-1", "B"), new SelectAnswer("option-2", "B")))));
        given(gradingWorkbookClient.getCorrectAnswers(workbookId))
                .willReturn(map);

        List<Boolean> results = solveService.gradeWorkbook(param);
        assertThat(results).hasSize(1);
        assertThat(results).containsExactly(true);
    }

    @Test
    @DisplayName("같은 정답이 중복으로 존재할 경우 전부 선택하지 않으면 틀린것으로 판단한다.")
    void testGradeProblemsForDuplicatedCorrectAnswer2() throws Exception {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                "고길동",
                List.of(
                        new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1", "B"))))
                )
        );

        Map<String, ProblemGrading> map = new HashMap<>();
        map.put("problem-1", new CheckBoxProblem("problem-1", ProblemType.CHECKBOX, new MultipleOptionAnswer(List.of(new SelectAnswer("option-1", "B"), new SelectAnswer("option-2", "B")))));

        given(gradingWorkbookClient.getCorrectAnswers(workbookId))
                .willReturn(map);

        List<Boolean> results = solveService.gradeWorkbook(param);
        assertThat(results).hasSize(1);
        assertThat(results).containsExactly(false);
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @DisplayName("풀이자 이름이 존재하지 않으면 채점을 할 수 없다.")
    void testGradeProblemForNotExistUserName(String name) {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                name,
                List.of(
                        new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1","개발")))),
                        new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-2","Apple"), new SelectAnswer("option-3", "Microsoft")))),
                        new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-4","Study")))),
                        new UserAnswer("problem-4", new Answer(List.of(new SelectAnswer("option-5","C"), new SelectAnswer("option-6", "B"))))
                )
        );

        assertThatThrownBy(() -> solveService.gradeWorkbook(param))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("풀이자 별명이 존재하지않아 채점할 수 없습니다.");
    }

    @ParameterizedTest
    @EmptySource
    @NullSource
    @DisplayName("문제에 대한 풀이가 존재하지 않으면 채점을 할 수 없다.")
    void testGradeProblemForEmptyAnswer(List<UserAnswer> problems) {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                "고길동",
                problems
        );

        assertThatThrownBy(() -> solveService.gradeWorkbook(param))
                .isInstanceOf(BadRequestDataException.class)
                .hasMessage("풀이가 존재하지 않아 채점할 수 없습니다.");
    }

    @Test
    @DisplayName("채점 후 결과를 WorkbookResult 에 저장한다.")
    void testSaveWorkbookResult() throws Exception {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                "고길동",
                List.of(
                        new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1","개발")))),
                        new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-2","Apple"), new SelectAnswer("option-3", "Microsoft")))),
                        new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-4","Study")))),
                        new UserAnswer("problem-4", new Answer(List.of(new SelectAnswer("option-5","A"), new SelectAnswer("option-6", "B"))))
                )
        );

        given(gradingWorkbookClient.getCorrectAnswers(workbookId))
                .willReturn(getCorrectAnswerMap());

        solveService.gradeWorkbook(param);
        verify(workbookResultRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("문제 풀이 후 문제집의 풀이 횟수를 증가하는 이벤트를 발행한다.")
    void testPublishEventForIncreaseSolvedCountInWorkbook() throws Exception {
        String workbookId = "workbook-id";
        SolveDto.WorkbookGradingParam param = new SolveDto.WorkbookGradingParam(
                "user-id",
                workbookId,
                "고길동",
                List.of(
                        new UserAnswer("problem-1", new Answer(List.of(new SelectAnswer("option-1","개발")))),
                        new UserAnswer("problem-2", new Answer(List.of(new SelectAnswer("option-2","Apple"), new SelectAnswer("option-3", "Microsoft")))),
                        new UserAnswer("problem-3", new Answer(List.of(new SelectAnswer("option-4","Study")))),
                        new UserAnswer("problem-4", new Answer(List.of(new SelectAnswer("option-5","A"), new SelectAnswer("option-6", "B"))))
                )
        );

        given(gradingWorkbookClient.getCorrectAnswers(workbookId))
                .willReturn(getCorrectAnswerMap());

        solveService.gradeWorkbook(param);
        verify(workbookEventsPublisher, times(1)).publish(any(EventsPublisherWorkbookEvent.SolvedCountAddEvent.class));
    }

    private Map<String, ProblemGrading> getCorrectAnswerMap() {
        Map<String, ProblemGrading> map = new HashMap<>();
        map.put("problem-1", new CheckBoxProblem("problem-1", ProblemType.CHECKBOX, new MultipleOptionAnswer(List.of(new SelectAnswer("option-1", "개발")))));
        map.put("problem-2", new CheckBoxProblem("problem-2", ProblemType.CHECKBOX, new MultipleOptionAnswer(List.of(new SelectAnswer("option-2", "Apple"), new SelectAnswer("option-3", "Microsoft")))));
        map.put("problem-3", new CheckBoxProblem("problem-3", ProblemType.CHECKBOX, new MultipleOptionAnswer(List.of(new SelectAnswer("option-4", "Study")))));
        map.put("problem-4", new CheckBoxProblem("problem-4", ProblemType.CHECKBOX, new MultipleOptionAnswer(List.of(new SelectAnswer("option-6", "B"), new SelectAnswer("option-7", "C")))));

        return map;
    }
}