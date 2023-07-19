package com.poolaeem.poolaeem.solve.presentation.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SolveResponse {
    @Getter
    public static class SolveResult {
        private String name;
        private int totalProblems;
        private int correctCount;
        private int accuracyRate;
        public SolveResult(String name, List<Boolean> results) {
            this.name = name;
            this.totalProblems = results.size();
            this.correctCount = (int) results.stream().filter(result -> result).count();
            this.accuracyRate = Math.round((float) correctCount / totalProblems) * 100;
        }
    }
}
