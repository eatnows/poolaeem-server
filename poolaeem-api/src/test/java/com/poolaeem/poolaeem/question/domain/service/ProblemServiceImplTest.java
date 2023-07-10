package com.poolaeem.poolaeem.question.domain.service;

import com.poolaeem.poolaeem.common.event.WorkbookEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.component.TextGenerator;
import com.poolaeem.poolaeem.question.domain.dto.ProblemDto;
import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.ProblemOption;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.question.infra.repository.ProblemOptionRepository;
import com.poolaeem.poolaeem.question.infra.repository.ProblemRepository;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import com.poolaeem.poolaeem.user.domain.entity.UserRole;
import com.poolaeem.poolaeem.user.domain.entity.vo.UserVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 문항 관리 서비스 테스트")
class ProblemServiceImplTest {
    @InjectMocks
    private ProblemServiceImpl problemService;
    @Mock
    private ProblemRepository problemRepository;
    @Mock
    private ProblemOptionRepository optionRepository;
    @Mock
    private WorkbookRepository workbookRepository;
    @Mock
    private WorkbookEventsPublisher workbookEventsPublisher;

    @Test
    @DisplayName("문제집에 문항을 추가할 수 있다.")
    void testCreateProblem() throws Exception {
        String workbookId = "workbook-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemCreateParam param = new ProblemDto.ProblemCreateParam(
                workbookId,
                userVo.getId(),
                "Word",
                List.of(new ProblemOptionDto("단어", true),
                        new ProblemOptionDto("세계", false),
                        new ProblemOptionDto("나무", false),
                        new ProblemOptionDto("우드", false),
                        new ProblemOptionDto("월드", false),
                        new ProblemOptionDto("워드", false),
                        new ProblemOptionDto("로드", false),
                        new ProblemOptionDto("위드", false),
                        new ProblemOptionDto("왈드", false)
                )
        );
        given(workbookRepository.findByIdAndIsDeletedFalse(workbookId))
                .willReturn(Optional.of(new Workbook(userVo.getId(), "문제집1", "설명", WorkbookTheme.PINK, 1)));
        given(problemRepository.save(any()))
                .willReturn(new Problem(null, "Word", 1));

        problemService.createProblem(param);

        verify(problemRepository, times(1)).save(any());
        verify(optionRepository, times(1)).saveAll(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    @DisplayName("문항에 문제는 1자 이상 100자 이하여야 한다.")
    void testCreateProblemForQuestionLength(int length) throws Exception {
        String workbookId = "workbookd-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemCreateParam param = new ProblemDto.ProblemCreateParam(
                workbookId,
                userVo.getId(),
                TextGenerator.generate(length),
                List.of(new ProblemOptionDto("단어", true),
                        new ProblemOptionDto("세계", false),
                        new ProblemOptionDto("나무", false),
                        new ProblemOptionDto("우드", false),
                        new ProblemOptionDto("월드", false),
                        new ProblemOptionDto("워드", false),
                        new ProblemOptionDto("로드", false),
                        new ProblemOptionDto("위드", false),
                        new ProblemOptionDto("왈드", false)
                )
        );

        assertThatThrownBy(() -> problemService.createProblem(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 11})
    @DisplayName("문항 선택지는 최소 2개, 최대 10개까지 추가할 수 있다.")
    void testCreateProblemForOptionSize(int size) throws Exception {
        String workbookId = "workbook-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemCreateParam param = new ProblemDto.ProblemCreateParam(
                workbookId,
                userVo.getId(),
                "Word",
                getOptions(size)
        );

        assertThatThrownBy(() -> problemService.createProblem(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    @DisplayName("문항 선택지의 값은 1자 이상 100자 이하로만 추가할 수 있다.")
    void testCreateProblemForOptionValueLength(int length) throws Exception {
        String workbookId = "workbook-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemCreateParam param = new ProblemDto.ProblemCreateParam(
                workbookId,
                userVo.getId(),
                "Word",
                List.of(new ProblemOptionDto("단어", true),
                        new ProblemOptionDto(TextGenerator.generate(length), false))
        );

        assertThatThrownBy(() -> problemService.createProblem(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("선택지는 최소 오답1개 정답1개를 가지고 있어야 한다.")
    void testCreateProblemForRequiredCorrectOption(boolean isCorrect) throws Exception {
        String workbookId = "workbook-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemCreateParam param = new ProblemDto.ProblemCreateParam(
                workbookId,
                userVo.getId(),
                "Word",
                List.of(new ProblemOptionDto("단어", isCorrect),
                        new ProblemOptionDto("단어2", isCorrect))
        );

        assertThatThrownBy(() -> problemService.createProblem(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @Test
    @DisplayName("문항을 추가하면 문제집 문항수를 1 증가 시키는 이벤트를 발행한다.")
    void testAddProblemCount() throws Exception {
        String workbookId = "workbook-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemCreateParam param = new ProblemDto.ProblemCreateParam(
                workbookId,
                userVo.getId(),
                "Word",
                List.of(new ProblemOptionDto("단어", true),
                        new ProblemOptionDto("세계", false)
                )
        );
        given(workbookRepository.findByIdAndIsDeletedFalse(workbookId))
                .willReturn(Optional.of(new Workbook(userVo.getId(), "문제집1", "설명", WorkbookTheme.PINK, 1)));
        given(problemRepository.save(any()))
                .willReturn(new Problem(null, "Word", 1));

        problemService.createProblem(param);

        verify(workbookEventsPublisher, times(1)).publish(any(EventsPublisherWorkbookEvent.ProblemAddEvent.class));
    }

    @Test
    @DisplayName("문항을 조회할 수 있다.")
    void testReadProblem() {
        String problemId = "problem-1";
        String userId = "user-1";

        Problem mockProblem = new Problem("problem-1", "Word",
                List.of(
                        new ProblemOption("option-1", "단어", true),
                        new ProblemOption("option-2", "세계", false)
                )
        );
        given(problemRepository.findByIdAndIsDeletedFalseAndUserId(problemId, userId))
                .willReturn(Optional.of(mockProblem));

        ProblemVo problem = problemService.readProblem(userId, problemId);

        assertThat(problem.getId()).isEqualTo(mockProblem.getId());
        assertThat(problem.getQuestion()).isEqualTo(mockProblem.getQuestion());
        assertThat(problem.getOptions()).hasSize(mockProblem.getOptions().size());
        assertThat(problem.getOptions().get(0).getOptionId()).isEqualTo(mockProblem.getOptions().get(0).getId());
    }

    @Test
    @DisplayName("문항을 수정할 수 있다.")
    void testUpdateProblem() {
        ProblemDto.ProblemUpdateParam param =
                new ProblemDto.ProblemUpdateParam(
                        "problem-id",
                        "user-id",
                        "Modify",
                        List.of(
                                new ProblemOptionDto("option-1", "변경", true),
                                new ProblemOptionDto("유지", false)
                        )
                );

        Problem mockProblem = new Problem("problem-id", "update", null);
        given(problemRepository.findByIdAndIsDeletedFalseAndUserId(anyString(), anyString()))
                .willReturn(Optional.of(mockProblem));
        given(optionRepository.findAllByProblemIdAndIsDeletedFalseOrderByOrderAsc(anyString()))
                .willReturn(List.of(
                        new ProblemOption("option-1", "변경", true),
                        new ProblemOption("option-2", "패치", false)));

        problemService.updateProblem(param);

        verify(optionRepository, times(1)).saveAll(any());
        verify(optionRepository, times(1)).deleteAll(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    @DisplayName("문항에 문제는 1자 이상 100자로만 수정할 수 있다.")
    void testUpdateProblemForQuestionLength(int length) throws Exception {
        String problemId = "problem-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemUpdateParam param = new ProblemDto.ProblemUpdateParam(
                problemId,
                userVo.getId(),
                TextGenerator.generate(length),
                List.of(new ProblemOptionDto("option-1", "단어", true),
                        new ProblemOptionDto("option-2", "세계", false),
                        new ProblemOptionDto("option-3", "나무", false),
                        new ProblemOptionDto("우드", false),
                        new ProblemOptionDto("월드", false),
                        new ProblemOptionDto("워드", false),
                        new ProblemOptionDto("로드", false),
                        new ProblemOptionDto("위드", false),
                        new ProblemOptionDto("왈드", false)
                )
        );

        assertThatThrownBy(() -> problemService.updateProblem(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 11})
    @DisplayName("문항 선택지는 최소 2개, 최대 10개까지 수정할 수 있다.")
    void testUpdateProblemForOptionSize(int size) throws Exception {
        String problemId = "problem-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemUpdateParam param = new ProblemDto.ProblemUpdateParam(
                problemId,
                userVo.getId(),
                "Word",
                getOptions(size)
        );

        assertThatThrownBy(() -> problemService.updateProblem(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    @DisplayName("문항 선택지의 값은 1자 이상 100자 이하로만 수정할 수 있다")
    void testUpdateProblemForOptionValueLength(int length) throws Exception {
        String problemId = "problem-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemUpdateParam param = new ProblemDto.ProblemUpdateParam(
                problemId,
                userVo.getId(),
                "Word",
                List.of(new ProblemOptionDto("option-1", "단어", true),
                        new ProblemOptionDto(TextGenerator.generate(length), false))
        );

        assertThatThrownBy(() -> problemService.updateProblem(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("선택지는 최소 오답1개 정답1개를 가지고 있어야 수정할 수 있다.")
    void testUpdateProblemForRequiredCorrectOption(boolean isCorrect) throws Exception {
        String problemId = "problem-id";
        UserVo userVo = new UserVo("user-id", null, null, UserRole.ROLE_USER, null, null, null, null);
        ProblemDto.ProblemUpdateParam param = new ProblemDto.ProblemUpdateParam(
                problemId,
                userVo.getId(),
                "Word",
                List.of(new ProblemOptionDto("option-1", "단어", isCorrect),
                        new ProblemOptionDto("단어2", isCorrect))
        );

        assertThatThrownBy(() -> problemService.updateProblem(param))
                .isInstanceOf(BadRequestDataException.class);
    }

    @Test
    @DisplayName("문항을 삭제할 수 있다. (문항을 삭제하면 문제집 문항수를 감소시키는 이벤트가 발생한다)")
    void testDeleteProblem() throws Exception {
        String userId = "user-id";
        String problemId = "problem-id";

        given(problemRepository.findByIdAndIsDeletedFalseAndUserId(problemId, userId))
                .willReturn(Optional.of(new Problem("problem-id",
                        new Workbook("workbook-id", "user-id", "문제집", "", 3, 2, WorkbookTheme.PINK),
                        "Computer"
                )));

        problemService.deleteProblem(userId, problemId);

        verify(optionRepository, times(1)).deleteAllByProblem(any());
        verify(problemRepository, times(1)).delete(any());
        verify(workbookEventsPublisher, times(1)).publish(any(EventsPublisherWorkbookEvent.ProblemDeleteEvent.class));
    }

    private List<ProblemOptionDto> getOptions(int size) {
        List<ProblemOptionDto> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new ProblemOptionDto("단어", true));
        }

        return list;
    }
}