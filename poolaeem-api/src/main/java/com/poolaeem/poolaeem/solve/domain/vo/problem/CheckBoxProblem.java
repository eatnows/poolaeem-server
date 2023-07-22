package com.poolaeem.poolaeem.solve.domain.vo.problem;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.MultipleOptionAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SelectAnswer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBoxProblem extends ProblemGrading {

    private final Map<String, SelectAnswer> answerMap = new HashMap<>();

    public CheckBoxProblem(String problemId, ProblemType type, MultipleOptionAnswer selectAnswers) {
        super(problemId, type, selectAnswers);

        for (SelectAnswer selectAnswer : selectAnswers.getValues()) {
            answerMap.put(selectAnswer.getOptionId(), selectAnswer);
        }
    }

    @Override
    public boolean grade(Answer userAnswers, List<AnswerResult> answerResults) {
        isCorrect = true;
        List<SelectAnswer> answers = userAnswers.getValues();
        if (answerMap.size() != answers.size()) {
            isCorrect = false;
        }

        for (SelectAnswer answer : answers) {
            if (!answerMap.containsKey(answer.getOptionId())) {
                isCorrect = false;
                answerResults.add(new AnswerResult(getProblemId(), answer.getAnswer(), false));
                continue;
            }
            answerResults.add(new AnswerResult(getProblemId(), answer.getAnswer(), true));
        }

        return isCorrect;
    }
}
