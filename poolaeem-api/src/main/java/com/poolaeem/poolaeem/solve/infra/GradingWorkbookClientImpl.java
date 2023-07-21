package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.question.application.ProblemService;
import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SelectAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.MultipleOptionAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SubjectiveAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.problem.CheckBoxProblem;
import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;
import com.poolaeem.poolaeem.solve.domain.vo.problem.SubjectiveProblem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GradingWorkbookClientImpl implements GradingWorkbookClient {
    private final ProblemService problemService;

    public GradingWorkbookClientImpl(ProblemService problemService) {
        this.problemService = problemService;
    }

    @Override
    public Map<String, ProblemGrading> getCorrectAnswers(String workbookId) {
        List<ProblemVo> problems = problemService.getCorrectAnswers(workbookId);
        return problems.stream()
                .map(problem -> {
                    // 문제 타입별로 문제채점 클래스를 생성
                    if (problem.getType() == ProblemType.CHECKBOX) {
                        return new CheckBoxProblem(problem.getProblemId(), problem.getType(),
                                new MultipleOptionAnswer(problem.getOptions().stream()
                                        .map(option -> new SelectAnswer(option.getOptionId(), option.getValue()))
                                        .collect(Collectors.toList())
                                )
                        );
                    }
                    return new SubjectiveProblem(problem.getProblemId(), problem.getType(), new SubjectiveAnswer(problem.getOptions().stream().map(ProblemOptionVo::getValue).toList()));
                })
                .collect(Collectors.toMap(ProblemGrading::getProblemId, Function.identity()));
    }

}
