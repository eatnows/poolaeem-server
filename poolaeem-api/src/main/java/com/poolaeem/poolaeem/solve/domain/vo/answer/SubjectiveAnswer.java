package com.poolaeem.poolaeem.solve.domain.vo.answer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectiveAnswer extends Answer {
    private Set<String> answers = new HashSet<>();
    public SubjectiveAnswer(List<String> answers) {
        super();

        for (String answer : answers) {
            this.answers.add(answer);
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    public boolean grade(String userAnswer) {
        return this.answers.contains(userAnswer);
    }
}
