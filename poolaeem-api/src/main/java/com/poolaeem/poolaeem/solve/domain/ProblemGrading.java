package com.poolaeem.poolaeem.solve.domain;

import com.poolaeem.poolaeem.question.domain.entity.ProblemType;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class ProblemGrading {
    private String problemId;
    private ProblemType type;
    private Set<String> correctAnswers = new HashSet<>();
    private boolean isCorrect;

    public ProblemGrading(String problemId, ProblemType type, List<String> correctAnswers) {
        this.problemId = problemId;
        this.type = type;

        this.correctAnswers = new HashSet<>();
        for (String answer : correctAnswers) {
            this.correctAnswers.add(answer);
        }
    }

    public boolean doGrading(List<String> userAnswers) {
        if (correctAnswers.size() != userAnswers.size()) {
            isCorrect = false;
            return false;
        }
        isCorrect = true;
        for (String answer : userAnswers) {
            if (!correctAnswers.contains(answer.toLowerCase())) {
                isCorrect = false;
                return false;
            }
        }

        return isCorrect;
    }
}
