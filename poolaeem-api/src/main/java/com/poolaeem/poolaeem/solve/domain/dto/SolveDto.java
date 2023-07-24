package com.poolaeem.poolaeem.solve.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SolveDto {
    @Getter
    public static class WorkbookGradingParam {
        private String userId;
        private String workbookId;
        private String name;
        private List<UserAnswer> problems = new ArrayList<>();

        public WorkbookGradingParam(String userId, String workbookId, String name, List<UserAnswer> problems) {
            this.userId = userId;
            this.workbookId = workbookId;
            this.name = name;
            this.problems = problems;
        }
    }
}