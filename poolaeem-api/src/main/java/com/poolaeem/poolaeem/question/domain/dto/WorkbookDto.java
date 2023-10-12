package com.poolaeem.poolaeem.question.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.poolaeem.poolaeem.question.domain.entity.WorkbookTheme;
import com.poolaeem.poolaeem.question.domain.entity.vo.WorkbookCreator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkbookDto {
    public record WorkbookCreateParam(
        String userId,
        String name,
        String description,
        WorkbookTheme theme
    ) {
    }

    public record WorkbookUpdateParam(
        String workbookId,
        String userId,
        String name,
        String description,
        WorkbookTheme theme
    ) {
    }

    public record SolveIntroductionRead(
        String workbookId,
        String name,
        String description,
        WorkbookTheme theme,
        WorkbookCreator creator,
        ZonedDateTime createdAt,
        int problemCount,
        int solvedCount
    ) {
    }

    public record WorkbookListRead(
        String workbookId,
        String name,
        String description,
        WorkbookTheme theme,
        @JsonFormat(pattern = TimeComponent.DATETIME_PATTERN, timezone = TimeComponent.DEFAULT_TIMEZONE)
        ZonedDateTime createdAt,
        int problemCount,
        int solvedCount
    ) {
    }
}
