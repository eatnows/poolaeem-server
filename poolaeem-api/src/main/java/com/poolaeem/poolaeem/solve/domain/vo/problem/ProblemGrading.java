package com.poolaeem.poolaeem.solve.domain.vo.problem;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import lombok.Getter;

import java.util.List;

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

    public abstract boolean grade(Answer userAnswers, List<AnswerResult> answerResults);

}
