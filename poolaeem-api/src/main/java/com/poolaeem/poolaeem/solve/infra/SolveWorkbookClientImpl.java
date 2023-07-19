package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.solve.domain.ProblemGrading;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SolveWorkbookClientImpl implements SolveWorkbookClient {
    private final ProblemService problemService;

    public SolveWorkbookClientImpl(ProblemService problemService) {
        this.problemService = problemService;
    }

    @Override
    public Map<String, ProblemGrading> getCorrectAnswers(String workbookId) {
        List<ProblemVo> problems = problemService.getCorrectAnswers(workbookId);
        // 정답 비교를 위해 소문자로 변환
        return problems.stream()
                .map(problem -> new ProblemGrading(problem.getProblemId(), problem.getType(), problem.getOptions().stream().map(option -> option.getValue().toLowerCase()).toList()))
                .collect(Collectors.toMap(ProblemGrading::getProblemId, Function.identity()));
    }
}
