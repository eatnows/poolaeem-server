package com.poolaeem.poolaeem.solve.infra;

import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemOptionVo;
import com.poolaeem.poolaeem.question.domain.entity.vo.ProblemVo;
import com.poolaeem.poolaeem.solve.domain.vo.answer.MultipleOptionAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SelectAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SubjectiveAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.problem.CheckBoxProblem;
import com.poolaeem.poolaeem.solve.domain.vo.problem.ProblemGrading;
import com.poolaeem.poolaeem.solve.domain.vo.problem.SubjectiveProblem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ProblemFactory {
    public static ProblemGrading makeProblem(ProblemVo problem) {
        switch (problem.getType()) {
            case CHECKBOX -> {
                return makeCheckBoxProblem(problem);
            }
            default -> {
                return makeSubjectiveProblem(problem);
            }
        }
    }
    private static SubjectiveProblem makeSubjectiveProblem(ProblemVo problem) {
        return new SubjectiveProblem(problem.getProblemId(), problem.getType(), new SubjectiveAnswer(problem.getOptions().stream().map(ProblemOptionVo::getValue).toList()));
    }
    private static CheckBoxProblem makeCheckBoxProblem(ProblemVo problem) {
        return new CheckBoxProblem(problem.getProblemId(), problem.getType(),
                new MultipleOptionAnswer(problem.getOptions().stream()
                        .map(option -> new SelectAnswer(option.getOptionId(), option.getValue()))
                        .toList()
                )
        );
    }
}
