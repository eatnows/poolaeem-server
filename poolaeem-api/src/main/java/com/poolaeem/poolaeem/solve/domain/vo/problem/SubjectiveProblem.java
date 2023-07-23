package com.poolaeem.poolaeem.solve.domain.vo.problem;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.entity.ProblemResult;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SubjectiveAnswer;

import java.util.ArrayList;
import java.util.List;

public class SubjectiveProblem extends ProblemGrading {

    public SubjectiveProblem(String problemId, ProblemType type, SubjectiveAnswer answer) {
        super(problemId, type, answer);
    }

    @Override
    public QuestionResultVo grade(Answer userAnswers) {
        String userValue = userAnswers.getValue();
        SubjectiveAnswer correctAnswer = (SubjectiveAnswer) getAnswer();
        isCorrect = true;

        List<AnswerResult> results = new ArrayList<>();
        if (correctAnswer.grade(userValue)) {
            results.add(new AnswerResult(getProblemId(), userValue, true));
        } else {
            isCorrect = false;
            results.add(new AnswerResult(getProblemId(), userValue, false));
        }

        return new QuestionResultVo(new ProblemResult(getProblemId(), isCorrect), results);
    }
}
