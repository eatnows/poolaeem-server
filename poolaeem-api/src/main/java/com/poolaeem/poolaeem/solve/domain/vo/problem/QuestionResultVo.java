package com.poolaeem.poolaeem.solve.domain.vo.problem;

import com.poolaeem.poolaeem.solve.domain.entity.AnswerResult;
import com.poolaeem.poolaeem.solve.domain.entity.ProblemResult;
import lombok.Getter;

import java.util.List;

@Getter
public class QuestionResultVo {
    private ProblemResult problemResult;
    private List<AnswerResult> answerResults;

    public QuestionResultVo(ProblemResult problemResult, List<AnswerResult> answerResults) {
        this.problemResult = problemResult;
        this.answerResults = answerResults;
    }
}
