package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.common.event.WorkbookEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.solve.application.GradingService;
import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;
import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;
import com.poolaeem.poolaeem.solve.domain.entity.WorkbookResult;
import com.poolaeem.poolaeem.solve.infra.GradingWorkbookClient;
import com.poolaeem.poolaeem.solve.infra.repository.WorkbookResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GradingServiceImpl implements GradingService {
    private final GradingWorkbookClient gradingWorkbookClient;
    private final WorkbookResultRepository workbookResultRepository;
    private final WorkbookEventsPublisher workbookEventsPublisher;

    public GradingServiceImpl(GradingWorkbookClient gradingWorkbookClient, WorkbookResultRepository workbookResultRepository, WorkbookEventsPublisher workbookEventsPublisher) {
        this.gradingWorkbookClient = gradingWorkbookClient;
        this.workbookResultRepository = workbookResultRepository;
        this.workbookEventsPublisher = workbookEventsPublisher;
    }

    @Transactional
    @Override
    public List<Boolean> gradeWorkbook(SolveDto.WorkbookGradingParam param) {
        validUserName(param.getName());
        validProblemEmpty(param.getProblems());

        String workbookId = param.getWorkbookId();
        List<Boolean> results = gradeProblems(workbookId, param.getProblems());
        saveResult(param, results);

        increaseSolveCountInWorkbook(workbookId);
        return results;
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

    private void saveResult(SolveDto.WorkbookGradingParam param, List<Boolean> results) {
        int correctCount = (int) results.stream().filter(Boolean::booleanValue).count();
        WorkbookResult workbookResult = new WorkbookResult(param.getWorkbookId(), param.getUserId(), param.getName(), results.size(), correctCount);
        workbookResultRepository.save(workbookResult);
    }

    private List<Boolean> gradeProblems(String workbookId, List<UserAnswer> userAnswers) {
        Map<String, ProblemGrading> correctAnswerMap = gradingWorkbookClient.getCorrectAnswers(workbookId);

        List<Boolean> results = new ArrayList<>();
        for (UserAnswer userAnswer : userAnswers) {
            ProblemGrading problem = correctAnswerMap.get(userAnswer.getProblemId());

            boolean result = problem.grade(userAnswer.getAnswer());
            results.add(result);
        }

        return results;
    }

}
