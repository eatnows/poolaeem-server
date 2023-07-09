package com.poolaeem.poolaeem.question.domain.service;

import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.component.TextGenerator;
import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.domain.dto.ProblemDto;
import com.poolaeem.poolaeem.question.domain.dto.ProblemOptionDto;
import com.poolaeem.poolaeem.question.domain.entity.Problem;
import com.poolaeem.poolaeem.question.domain.entity.Workbook;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.infra.repository.ProblemOptionRepository;
import com.poolaeem.poolaeem.question.infra.repository.ProblemRepository;
import com.poolaeem.poolaeem.question.infra.repository.WorkbookRepository;
import com.poolaeem.poolaeem.question.presentation.dto.ProblemRequest;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    @DisplayName("문제집에 문항을 추가할 수 있다.")
    void testCreateProblem() throws Exception {
        String workbookId = "";
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
        String workbookId = "";
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
        String workbookId = "";
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
        String workbookId = "";
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
        String workbookId = "";
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

    private List<ProblemOptionDto> getOptions(int size) {
        List<ProblemOptionDto> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(new ProblemOptionDto("단어", true));
        }

        return list;
    }
}