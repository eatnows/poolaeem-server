package com.poolaeem.poolaeem.solve.domain.vo.problem;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SubjectiveAnswer;

public class SubjectiveProblem extends ProblemGrading {

    public SubjectiveProblem(String problemId, ProblemType type, SubjectiveAnswer answer) {
        super(problemId, type, answer);
    }

    @Override
    public boolean grade(Answer userAnswers) {
        String userValue = userAnswers.getValue();
        SubjectiveAnswer correctAnswer = (SubjectiveAnswer) getAnswer();

        return correctAnswer.grade(userValue);
    }
}
