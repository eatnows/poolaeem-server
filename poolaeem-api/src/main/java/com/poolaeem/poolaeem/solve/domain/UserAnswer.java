package com.poolaeem.poolaeem.solve.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserAnswer {
    private String problemId;
    private List<String> answers = new ArrayList<>();

    public UserAnswer(String problemId, List<String> answers) {
        this.problemId = problemId;
        this.answers = answers;
    }
}
