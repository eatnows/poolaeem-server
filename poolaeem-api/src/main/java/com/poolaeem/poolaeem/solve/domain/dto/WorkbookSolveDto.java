package com.poolaeem.poolaeem.solve.domain.dto;

import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
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
        private WorkbookTheme theme;
        private WorkbookCreator creator;
        private ZonedDateTime createdAt;
        private int problemCount;
        private int solvedCount;

        public SolveInfoRead(String workbookId, String name, String description, WorkbookTheme theme, WorkbookCreator creator, ZonedDateTime createdAt, int problemCount, int solvedCount) {
            this.workbookId = workbookId;
            this.name = name;
            this.description = description;
            this.theme = theme;
            this.creator = creator;
            this.createdAt = createdAt;
            this.problemCount = problemCount;
            this.solvedCount = solvedCount;
        }

        public void convertUtcToKst() {
            this.createdAt = TimeComponent.convertUtcToKst(createdAt);
        }
    }
}
