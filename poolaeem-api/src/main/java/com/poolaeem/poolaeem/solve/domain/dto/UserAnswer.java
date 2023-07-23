package com.poolaeem.poolaeem.solve.domain.dto;

import com.poolaeem.poolaeem.solve.domain.vo.answer.Answer;
import lombok.Getter;

@Getter
public class UserAnswer {
    private String problemId;
    private Answer answer;

    public UserAnswer(String problemId, Answer answer) {
        this.problemId = problemId;
        this.answer = answer;
    }
}
