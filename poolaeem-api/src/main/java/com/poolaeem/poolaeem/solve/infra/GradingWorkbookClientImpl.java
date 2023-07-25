package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.application.WorkbookService;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GradingWorkbookClientImpl implements GradingWorkbookClient {
    private final ProblemService problemService;
    private final WorkbookService workbookService;

    public GradingWorkbookClientImpl(ProblemService problemService, WorkbookService workbookService) {
        this.problemService = problemService;
        this.workbookService = workbookService;
    }

    @Override
    public Map<String, ProblemGrading> getCorrectAnswers(String workbookId) {
        List<ProblemVo> problems = problemService.getCorrectAnswers(workbookId);
        return problems.stream()
                .map(ProblemFactory::makeProblem)
                .collect(Collectors.toMap(ProblemGrading::getProblemId, Function.identity()));
    }

    @Override
    public void validWorkbookManage(String workbookId, String reqUserId) {
        workbookService.validWorkbookManage(workbookId, reqUserId);
    }
}
