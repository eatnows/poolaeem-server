package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.common.event.WorkbookEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.solve.application.SolveService;
import com.poolaeem.poolaeem.solve.domain.ProblemGrading;
import com.poolaeem.poolaeem.solve.domain.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;
import com.poolaeem.poolaeem.solve.domain.entity.WorkbookResult;
import com.poolaeem.poolaeem.solve.infra.SolveWorkbookClient;
import com.poolaeem.poolaeem.solve.infra.repository.WorkbookResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SolveServiceImpl implements SolveService {
    private final SolveWorkbookClient solveWorkbookClient;
    private final WorkbookResultRepository workbookResultRepository;
    private final WorkbookEventsPublisher workbookEventsPublisher;

    public SolveServiceImpl(SolveWorkbookClient solveWorkbookClient, WorkbookResultRepository workbookResultRepository, WorkbookEventsPublisher workbookEventsPublisher) {
        this.solveWorkbookClient = solveWorkbookClient;
        this.workbookResultRepository = workbookResultRepository;
        this.workbookEventsPublisher = workbookEventsPublisher;
    }

    @Transactional
    @Override
    public List<Boolean> solveWorkbook(SolveDto.WorkbookSolveParam param) {
        validUserName(param.getUserId(), param.getName());

        String workbookId = param.getWorkbookId();
        List<Boolean> results = doGrading(workbookId, param.getProblems());
        saveResult(param, results);

        increaseSolveCountInWorkbook(workbookId);
        return results;
    }

    private void increaseSolveCountInWorkbook(String workbookId) {
        workbookEventsPublisher.publish(new EventsPublisherWorkbookEvent.SolvedCountAddEvent(workbookId));
    }

    private void validUserName(String userId, String name) {
        if (userId == null && !StringUtils.hasText(name)) {
            throw new BadRequestDataException("유저 정보가 없어서 채점할 수 없습니다.");
        }
    }

    private void saveResult(SolveDto.WorkbookSolveParam param, List<Boolean> results) {
        String name = param.getName();
        String userId = param.getUserId();

        int correctCount = (int) results.stream().filter(Boolean::booleanValue).count();
        WorkbookResult workbookResult = new WorkbookResult(param.getWorkbookId(), userId, name, results.size(), correctCount);
        workbookResultRepository.save(workbookResult);
    }

    private List<Boolean> doGrading(String workbookId, List<UserAnswer> userAnswers) {
        Map<String, ProblemGrading> correctAnswerMap = solveWorkbookClient.getCorrectAnswers(workbookId);
        List<Boolean> results = new ArrayList<>();
        for (UserAnswer userAnswer : userAnswers) {
            ProblemGrading problem = correctAnswerMap.get(userAnswer.getProblemId());
            boolean result = problem.doGrading(userAnswer.getAnswers());
            results.add(result);
        }

        return results;
    }

}
