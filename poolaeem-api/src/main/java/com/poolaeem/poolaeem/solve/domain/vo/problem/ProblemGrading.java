package com.poolaeem.poolaeem.solve.domain.vo.problem;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import lombok.Getter;

@Getter
public abstract class ProblemGrading {
    private String problemId;
    private ProblemType type;
    private Answer answer;
    protected boolean isCorrect;

    protected ProblemGrading(String problemId, ProblemType type, Answer answer) {
        this.problemId = problemId;
        this.type = type;
        this.answer = answer;
    }

    public abstract QuestionResultVo grade(Answer userAnswers);
}
