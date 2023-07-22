package com.poolaeem.poolaeem.solve.domain.vo.problem;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SubjectiveAnswer;

import java.util.List;

public class SubjectiveProblem extends ProblemGrading {

    public SubjectiveProblem(String problemId, ProblemType type, SubjectiveAnswer answer) {
        super(problemId, type, answer);
    }

    @Override
    public boolean grade(Answer userAnswers, List<AnswerResult> answerResults) {
        String userValue = userAnswers.getValue();
        SubjectiveAnswer correctAnswer = (SubjectiveAnswer) getAnswer();

        isCorrect = false;
        if (correctAnswer.grade(userValue)) {
            isCorrect = true;
        }

        answerResults.add(new AnswerResult(getProblemId(), userValue, isCorrect));

        return isCorrect;
    }
}
