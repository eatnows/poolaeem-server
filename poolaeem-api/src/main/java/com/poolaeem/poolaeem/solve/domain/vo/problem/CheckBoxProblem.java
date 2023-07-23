package com.poolaeem.poolaeem.solve.domain.vo.problem;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.entity.ProblemResult;
import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.MultipleOptionAnswer;
import com.poolaeem.poolaeem.solve.domain.vo.answer.SelectAnswer;

import java.util.ArrayList;
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
    public QuestionResultVo grade(Answer userAnswers) {
        List<SelectAnswer> answers = userAnswers.getValues();
        isCorrect = true;

        List<AnswerResult> results = new ArrayList<>();
        for (SelectAnswer answer : answers) {
            if (answerMap.containsKey(answer.getOptionId())) {
                results.add(new AnswerResult(getProblemId(), answer.getAnswer(), true));
                continue;
            }
            isCorrect = false;
            results.add(new AnswerResult(getProblemId(), answer.getAnswer(), false));
        }

        return new QuestionResultVo(new ProblemResult(getProblemId(), getResultOfProblem(results)), results);
    }

    private boolean getResultOfProblem(List<AnswerResult> answerResults) {
        if (answerMap.size() != answerResults.size()) {
            isCorrect = false;
        }
        return isCorrect;
    }
}
