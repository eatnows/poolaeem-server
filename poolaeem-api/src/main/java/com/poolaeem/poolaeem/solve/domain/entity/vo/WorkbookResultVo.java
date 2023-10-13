package com.poolaeem.poolaeem.solve.domain.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poolaeem.poolaeem.common.component.time.TimeComponent;
import com.querydsl.core.annotations.QueryProjection;

import java.time.ZonedDateTime;

public record WorkbookResultVo(
    String resultId,
    @JsonIgnore
    String workbookId,
    String userId,
    String userName,
    Integer totalQuestions,
    Integer correctCount,
    @JsonFormat(pattern = TimeComponent.DATETIME_PATTERN, timezone = TimeComponent.DEFAULT_TIMEZONE)
    ZonedDateTime solvedAt
) {
    @QueryProjection
    public WorkbookResultVo(String resultId, String userName, Integer totalQuestions, Integer correctCount, ZonedDateTime solvedAt) {
        this(resultId, null, null, userName, totalQuestions, correctCount, solvedAt);
    }
}
