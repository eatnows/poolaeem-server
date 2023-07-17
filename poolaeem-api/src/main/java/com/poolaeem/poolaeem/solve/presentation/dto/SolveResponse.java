package com.poolaeem.poolaeem.solve.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.solve.domain.dto.WorkbookSolveDto;
import com.poolaeem.poolaeem.solve.domain.dto.WorkbookCreator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SolveResponse {
    @Getter
    public static class SolveInfoRead {
        private String name;
        private String description;
        private WorkbookTheme theme;
        private WorkbookCreator creator;
        @JsonFormat(pattern = TimeComponent.DATETIME_PATTERN, timezone = TimeComponent.DEFAULT_TIMEZONE)
        private ZonedDateTime createdAt;
        private int problemCount;
        private int solvedCount;

        public SolveInfoRead(WorkbookSolveDto.SolveInfoRead info) {
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
