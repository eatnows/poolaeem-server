package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.common.event.WorkbookEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.solve.application.GradingService;
import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.entity.ProblemResult;
import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;
import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;
import com.poolaeem.poolaeem.solve.domain.entity.WorkbookResult;
import com.poolaeem.poolaeem.solve.infra.GradingWorkbookClient;
import com.poolaeem.poolaeem.solve.infra.repository.WorkbookResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GradingServiceImpl implements GradingService {
    private final GradingWorkbookClient gradingWorkbookClient;
    private final WorkbookResultRepository workbookResultRepository;
    private final WorkbookEventsPublisher workbookEventsPublisher;
    private final ResultRecord resultRecord;

    public GradingServiceImpl(GradingWorkbookClient gradingWorkbookClient, WorkbookResultRepository workbookResultRepository, WorkbookEventsPublisher workbookEventsPublisher, ResultRecord resultRecord) {
        this.gradingWorkbookClient = gradingWorkbookClient;
        this.workbookResultRepository = workbookResultRepository;
        this.workbookEventsPublisher = workbookEventsPublisher;
        this.resultRecord = resultRecord;
    }

    @Transactional
    @Override
    public List<Boolean> gradeWorkbook(SolveDto.WorkbookGradingParam param) {
        validUserName(param.getName());
        validProblemEmpty(param.getProblems());
        String workbookId = param.getWorkbookId();
        List<AnswerResult> answerResults = new ArrayList<>();

        List<ProblemResult> results = gradeProblems(workbookId, param.getProblems(), answerResults);

        saveResult(param, results, answerResults);
        increaseSolveCountInWorkbook(workbookId);

        return results.stream().map(ProblemResult::getIsCorrect).toList();
    }

    private void saveResult(SolveDto.WorkbookGradingParam param, List<ProblemResult> results, List<AnswerResult> answerResults) {
        saveWorkbookResult(param, results);
        asyncSaveAllResult(param.getUserId(), results, answerResults);
    }

    private void asyncSaveAllResult(String userId, List<ProblemResult> problemResults, List<AnswerResult> answerResults) {
        if (userId == null) return;

        resultRecord.asyncSaveProblemResult(problemResults);
        resultRecord.asyncSaveAnswerResult(answerResults);
    }

    private void validProblemEmpty(List<UserAnswer> problems) {
        if (CollectionUtils.isEmpty(problems)) {
            throw new BadRequestDataException("풀이가 존재하지 않아 채점할 수 없습니다.");
        }
    }

    private void increaseSolveCountInWorkbook(String workbookId) {
        workbookEventsPublisher.publish(new EventsPublisherWorkbookEvent.SolvedCountAddEvent(workbookId));
    }

    private void validUserName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new BadRequestDataException("풀이자 별명이 존재하지않아 채점할 수 없습니다.");
        }
    }

    private void saveWorkbookResult(SolveDto.WorkbookGradingParam param, List<ProblemResult> results) {
        int correctCount = (int) results.stream().filter(ProblemResult::getIsCorrect).count();
        WorkbookResult workbookResult = new WorkbookResult(param.getWorkbookId(), param.getUserId(), param.getName(), results.size(), correctCount);
        workbookResultRepository.save(workbookResult);
    }

    private List<ProblemResult> gradeProblems(String workbookId, List<UserAnswer> userAnswers, List<AnswerResult> answerResults) {
        Map<String, ProblemGrading> correctAnswerMap = gradingWorkbookClient.getCorrectAnswers(workbookId);

        List<ProblemResult> results = new ArrayList<>();
        for (UserAnswer userAnswer : userAnswers) {
            ProblemGrading problem = correctAnswerMap.get(userAnswer.getProblemId());

            boolean result = problem.grade(userAnswer.getAnswer(), answerResults);
            results.add(new ProblemResult(problem.getProblemId(), result));
        }

        return results;
    }

}
