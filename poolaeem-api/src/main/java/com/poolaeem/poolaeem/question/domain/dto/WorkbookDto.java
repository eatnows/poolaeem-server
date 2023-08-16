package com.poolaeem.poolaeem.question.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookCreator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookDto {

    @Getter
    public static class WorkbookCreateParam {
        private String userId;
        private String name;
        private String description;
        private WorkbookTheme theme;

        public WorkbookCreateParam(String userId, String name, String description, WorkbookTheme theme) {
            this.userId = userId;
            this.name = name;
            this.description = description;
            this.theme = theme;
        }
    }

    @Getter
    public static final class WorkbookUpdateParam {
        private String workbookId;
        private String userId;
        private String name;
        private String description;

        public WorkbookUpdateParam(String workbookId, String userId, String name, String description) {
            this.workbookId = workbookId;
            this.userId = userId;
            this.name = name;
            this.description = description;
        }
    }

    @Getter
    public static class SolveIntroductionRead {
        private String workbookId;
        private String name;
        private String description;
        private WorkbookTheme theme;
        private WorkbookCreator creator;
        private ZonedDateTime createdAt;
        private int problemCount;
        private int solvedCount;

        public SolveIntroductionRead(String workbookId, String name, String description, WorkbookTheme theme, WorkbookCreator creator, ZonedDateTime createdAt, int problemCount, int solvedCount) {
            this.workbookId = workbookId;
            this.name = name;
            this.description = description;
            this.theme = theme;
            this.creator = creator;
            this.createdAt = createdAt;
            this.problemCount = problemCount;
            this.solvedCount = solvedCount;
        }
    }

    @Getter
    public static class WorkbookListRead {
        private String workbookId;
        private String name;
        private String description;
        private WorkbookTheme theme;
        @JsonFormat(pattern = TimeComponent.DATETIME_PATTERN, timezone = TimeComponent.DEFAULT_TIMEZONE)
        private ZonedDateTime createdAt;
        private int problemCount;
        private int solvedCount;

        public WorkbookListRead(String workbookId, String name, String description, WorkbookTheme theme, ZonedDateTime createdAt, int problemCount, int solvedCount) {
            this.workbookId = workbookId;
            this.name = name;
            this.description = description;
            this.theme = theme;
            this.createdAt = createdAt;
            this.problemCount = problemCount;
            this.solvedCount = solvedCount;
        }
    }
}
