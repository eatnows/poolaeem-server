package com.poolaeem.poolaeem.solve.presentation.dto;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradingResponse {
    public record GradingResult(
            String name,
            int totalProblems,
            int correctCount,
            int accuracyRate
    ) {
        public GradingResult(String name, List<Boolean> results) {
            this(name,
                    results.size(),
                    (int) results.stream().filter(result -> result).count(),
                    Math.round((float) (int) results.stream().filter(result -> result).count() / results.size() * 100));
        }
    }
}
