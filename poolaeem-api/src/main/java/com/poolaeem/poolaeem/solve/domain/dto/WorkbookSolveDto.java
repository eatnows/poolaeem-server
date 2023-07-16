package com.poolaeem.poolaeem.solve.domain.dto;

import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookSolveDto {
    @Getter
    public static class SolveInfoRead {
        private String workbookId;
        private String name;
        private String description;
        private WorkbookAuthor author;
        private ZonedDateTime createdAt;
        private int problemCount;
        private int solvedCount;

        public SolveInfoRead(String workbookId, String name, String description, WorkbookAuthor author, ZonedDateTime createdAt, int problemCount, int solvedCount) {
            this.workbookId = workbookId;
            this.name = name;
            this.description = description;
            this.author = author;
            this.createdAt = createdAt;
            this.problemCount = problemCount;
            this.solvedCount = solvedCount;
        }

        public void convertUtcToKst() {
            this.createdAt = TimeComponent.convertUtcToKst(createdAt);
        }
    }
}
