package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.entity.ProblemResult;
import com.poolaeem.poolaeem.solve.infra.repository.AnswerResultRepository;
import com.poolaeem.poolaeem.solve.infra.repository.ProblemResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ResultRecord {
    private final ProblemResultRepository problemResultRepository;
    private final AnswerResultRepository answerResultRepository;

    public ResultRecord(ProblemResultRepository problemResultRepository, AnswerResultRepository answerResultRepository) {
        this.problemResultRepository = problemResultRepository;
        this.answerResultRepository = answerResultRepository;
    }

    @Async("asyncThreadPoolTaskExecutor")
    @Transactional
    public void asyncSaveProblemResult(List<ProblemResult> problemResults) {
        saveAllProblemResults(problemResults);
    }

    @Async("asyncThreadPoolTaskExecutor")
    @Transactional
    public void asyncSaveAnswerResult(List<AnswerResult> answerResults) {
        saveAllAnswerResults(answerResults);
    }

    private void saveAllAnswerResults(List<AnswerResult> answerResults) {
        answerResultRepository.saveAll(answerResults);
    }

    private void saveAllProblemResults(List<ProblemResult> problemResults) {
        problemResultRepository.saveAll(problemResults);
    }
}
