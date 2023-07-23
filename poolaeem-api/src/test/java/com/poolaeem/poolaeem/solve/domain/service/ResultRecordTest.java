package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.entity.ProblemResult;
import com.poolaeem.poolaeem.solve.infra.repository.AnswerResultRepository;
import com.poolaeem.poolaeem.solve.infra.repository.ProblemResultRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("단위: 채점 결과 저장 테스트")
class ResultRecordTest {
    @InjectMocks
    private ResultRecord resultRecord;
    @Mock
    private ProblemResultRepository problemResultRepository;
    @Mock
    private AnswerResultRepository answerResultRepository;

    @Test
    @DisplayName("문항의 채점 결과를 저장할 수 있다.")
    void testSaveAllProblemResults() {
        List<ProblemResult> results = Arrays.asList(
                new ProblemResult("problem-1", true),
                new ProblemResult("problem-2", false),
                new ProblemResult("problem-3", true)
        );
        resultRecord.asyncSaveProblemResult(results);

        verify(problemResultRepository, times(1)).saveAll(results);
        verify(answerResultRepository, times(0)).saveAll(any());
    }

    @Test
    @DisplayName("문항별 답안 채점 결과를 저장할 수 있다.")
    void testSaveAllAnswersByProblem() {
        List<AnswerResult> results = Arrays.asList(
                new AnswerResult("problem-1", "답안1", true),
                new AnswerResult("problem-1", "답안2", true),
                new AnswerResult("problem-2", "답안3", false),
                new AnswerResult("problem-3", "답안4", true)
        );

        resultRecord.asyncSaveAnswerResult(results);

        verify(answerResultRepository, times(1)).saveAll(results);
        verify(problemResultRepository, times(0)).saveAll(any());
    }
}