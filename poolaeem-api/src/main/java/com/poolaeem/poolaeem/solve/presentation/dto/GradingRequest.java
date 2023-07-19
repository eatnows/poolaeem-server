package com.poolaeem.poolaeem.solve.presentation.dto;

import com.poolaeem.poolaeem.solve.domain.UserAnswer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradingRequest {

    @Getter
    public static class WorkbookGrade {
        private String name;
        private List<UserAnswer> problems;

        public WorkbookGrade(String name, List<UserAnswer> problems) {
            this.name = name;
            this.problems = problems;
        }
    }
}
