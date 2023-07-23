package com.poolaeem.poolaeem.question.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.dto.WorkbookDto;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookCreator;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookVo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookResponse {

    @Getter
    public static class WorkbookCreate {
        private String workbookId;

        public WorkbookCreate(String workbookId) {
            this.workbookId = workbookId;
        }
    }

    @Getter
    public static class WorkbookInfoRead {
        private String name;
        private String description;
        private int problemCount;
        private int solvedCount;
        private WorkbookTheme theme;

        public WorkbookInfoRead(WorkbookVo workbook) {
            this.name = workbook.getName();
            this.description = workbook.getDescription();
            this.problemCount = workbook.getProblemCount();
            this.solvedCount = workbook.getSolvedCount();
            this.theme = workbook.getTheme();
        }
    }

    @Getter
    public static class SolveIntroductionRead {
        private String name;
        private String description;
        private WorkbookTheme theme;
        private WorkbookCreator creator;
        @JsonFormat(pattern = TimeComponent.DATETIME_PATTERN, timezone = TimeComponent.DEFAULT_TIMEZONE)
        private ZonedDateTime createdAt;
        private int problemCount;
        private int solvedCount;

        public SolveIntroductionRead(WorkbookDto.SolveIntroductionRead info) {
            this.name = info.getName();
            this.description = info.getDescription();
            this.theme = info.getTheme();
            this.creator = info.getCreator();
            this.createdAt = info.getCreatedAt();
            this.problemCount = info.getProblemCount();
            this.solvedCount = info.getSolvedCount();
        }
    }
}
