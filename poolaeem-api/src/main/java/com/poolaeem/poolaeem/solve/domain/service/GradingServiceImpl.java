package com.poolaeem.poolaeem.solve.domain.service;

import com.poolaeem.poolaeem.common.event.WorkbookEventsPublisher;
import com.poolaeem.poolaeem.common.event.obj.EventsPublisherWorkbookEvent;
import com.poolaeem.poolaeem.common.exception.request.BadRequestDataException;
import com.poolaeem.poolaeem.solve.application.GradingService;
import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.entity.ProblemResult;
import com.poolaeem.poolaeem.solve.domain.validation.GradeValidation;
import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;
import com.poolaeem.poolaeem.solve.domain.dto.UserAnswer;
import com.poolaeem.poolaeem.solve.domain.dto.SolveDto;
import com.poolaeem.poolaeem.solve.domain.entity.WorkbookResult;
import com.poolaeem.poolaeem.solve.domain.vo.problem.QuestionResultVo;
import com.poolaeem.poolaeem.solve.infra.GradingWorkbookClient;
import com.poolaeem.poolaeem.solve.infra.repository.WorkbookResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
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

        List<QuestionResultVo> results = gradeProblems(workbookId, param.getProblems());

        saveResult(param, results);
        increaseSolveCountInWorkbook(workbookId);

        return results.stream().map(QuestionResultVo::getProblemResult).map(ProblemResult::getIsCorrect).toList();
    }

    private void saveResult(SolveDto.WorkbookGradingParam param, List<QuestionResultVo> results) {
        List<ProblemResult> problemResults = results.stream().map(QuestionResultVo::getProblemResult).toList();
        List<AnswerResult> answerResults = results.stream().map(QuestionResultVo::getAnswerResults).flatMap(Collection::stream).toList();

        saveWorkbookResult(param, problemResults);
        asyncSaveAllResult(param.getUserId(), problemResults, answerResults);
    }

    private void asyncSaveAllResult(String userId, List<ProblemResult> problemResults, List<AnswerResult> answerResults) {
        if (userId == null) return;

        resultRecord.asyncSaveProblemResult(problemResults);
        resultRecord.asyncSaveAnswerResult(answerResults);
    }

    private void validProblemEmpty(List<UserAnswer> problems) {
        if (CollectionUtils.isEmpty(problems)) {
            throw new BadRequestDataException(GradeValidation.Message.EMPTY_PROBLEMS);
        }
    }

    private void increaseSolveCountInWorkbook(String workbookId) {
        workbookEventsPublisher.publish(new EventsPublisherWorkbookEvent.SolvedCountAddEvent(workbookId));
    }

    private void validUserName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new BadRequestDataException(GradeValidation.Message.SOLVED_USER_NAME_NOT_FOUND);
        }
        if (name.length() > GradeValidation.SOLVED_USER_NAME_MAX_LENGTH) {
            throw new BadRequestDataException(GradeValidation.Message.USER_NAME_LENGTH);
        }
    }

    private void saveWorkbookResult(SolveDto.WorkbookGradingParam param, List<ProblemResult> results) {
        int correctCount = (int) results.stream().filter(ProblemResult::getIsCorrect).count();
        WorkbookResult workbookResult = new WorkbookResult(param.getWorkbookId(), param.getUserId(), param.getName(), results.size(), correctCount);
        workbookResultRepository.save(workbookResult);
    }

    private List<QuestionResultVo> gradeProblems(String workbookId, List<UserAnswer> userAnswers) {
        Map<String, ProblemGrading> correctAnswerMap = gradingWorkbookClient.getCorrectAnswers(workbookId);

        List<QuestionResultVo> results = new ArrayList<>();
        for (UserAnswer userAnswer : userAnswers) {
            ProblemGrading problem = correctAnswerMap.get(userAnswer.getProblemId());

            QuestionResultVo result = problem.grade(userAnswer.getAnswer());
            results.add(result);
        }

        return results;
    }

}
